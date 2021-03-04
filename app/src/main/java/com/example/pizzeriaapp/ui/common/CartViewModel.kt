package com.example.pizzeriaapp.ui.common

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizzeriaapp.data.cart.CartItemInterface
import com.example.pizzeriaapp.data.cart.FoodType
import com.example.pizzeriaapp.data.cart.beverage.BeverageCartItem
import com.example.pizzeriaapp.data.cart.burger.BurgerCartItem
import com.example.pizzeriaapp.data.cart.pasta.PastaCartItem
import com.example.pizzeriaapp.data.cart.payment.PaymentMethod
import com.example.pizzeriaapp.data.cart.pita.PitaCartItem
import com.example.pizzeriaapp.data.cart.pizza.PizzaCartItem
import com.example.pizzeriaapp.data.cart.salad.SaladCartItem
import com.example.pizzeriaapp.data.order.responses.OrderResponse
import com.example.pizzeriaapp.data.preferences.DataStoreRepository
import com.example.pizzeriaapp.data.preferences.UserInfo
import com.example.pizzeriaapp.data.usecases.CreateOrder
import com.example.pizzeriaapp.data.utils.Resource
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CartViewModel @ViewModelInject constructor(
    val tokenManager: TokenManager,
    val requestResponseTokenValidator: RequestResponseTokenValidator,
    val dataStoreRepository: DataStoreRepository,
    val createOrder: CreateOrder
) : ViewModel() {

    val cartItems = MutableLiveData<List<CartItemInterface>>()

    val cartItemsAmount = MutableLiveData<Int>()

    val userInfo = dataStoreRepository.getUserInfo()

    lateinit var info: UserInfo

    init {
        observeUserInfo()
    }

    private fun observeUserInfo() {
        viewModelScope.launch(IO) {
            dataStoreRepository.getUserInfo().collect {
                info = it
            }
        }
    }

    fun setUserInfo(
        streetName: String,
        houseNumber: String,
        cityName: String,
        phoneNumber: String
    ) {
        viewModelScope.launch(IO) {
            dataStoreRepository.saveUserInfo(streetName, houseNumber, cityName, phoneNumber)
        }
    }

    val price = MutableLiveData<Float>(0f)

    var streetNameInput: String? = null
    var houseNumberInput: String? = null
    var cityNameInput: String? = null
    var phoneNumberInput: String? = null
    var paymentMethod: PaymentMethod? = null

    fun addItem(item: CartItemInterface) {
        val currentItems = cartItems.value
        if (currentItems != null) {
            val exists = currentItems
                .filter { it.getFoodType() == item.getFoodType() }
                .filter {
                    when (it.getFoodType()) {
                        is FoodType.Pizza -> return@filter it as PizzaCartItem == item as PizzaCartItem
                        is FoodType.Burger -> return@filter it as BurgerCartItem == item as BurgerCartItem
                        is FoodType.Pasta -> return@filter it as PastaCartItem == item as PastaCartItem
                        is FoodType.Pita -> return@filter it as PitaCartItem == item as PitaCartItem
                        is FoodType.Salad -> return@filter it as SaladCartItem == item as SaladCartItem
                        is FoodType.Beverage -> return@filter it as BeverageCartItem == item as BeverageCartItem
                    }
                }
            if (exists.isNotEmpty()) {
                increaseAmount(item)
            } else {
                val items = mutableListOf<CartItemInterface>()
                items.addAll(currentItems)
                items.add(item)
                cartItems.value = items
            }
        } else {
            cartItems.value = listOf(item)
        }
        reactToCartChange()
    }

    fun removeItem(item: CartItemInterface) {
        cartItems.value?.also { items ->
            val filtered = items.filter { it != item }
            cartItems.value = filtered
            reactToCartChange()
        }
    }

    fun increaseAmount(item: CartItemInterface) {
        val items = cartItems.value!!.map {
            if (it == item) {
                it.amount++
            }
            return@map it
        }
        cartItems.value = items
        reactToCartChange()
    }

    fun decreaseAmount(item: CartItemInterface) {
        val items = cartItems.value!!.map {
            if (it == item) {
                it.amount--
            }
            return@map it
        }.filter { it.amount > 0 }
        cartItems.value = items
        reactToCartChange()
    }

    fun clearCart() {
        cartItems.postValue(listOf())
        price.postValue(0f)
        cartItemsAmount.postValue(0)
    }

    private fun reactToCartChange() {
        setPrice()
        setCartItemsAmount()
    }

    private fun setPrice() {
        var sum = 0f
        cartItems.value?.forEach { sum += it.getPrice() }
        price.postValue(sum)
    }

    private fun setCartItemsAmount() {
        val amount = cartItems.value?.sumBy { it.amount }
        cartItemsAmount.postValue(amount)
    }

    lateinit var orderRequest: Channel<Resource<OrderResponse?>>

    fun setOrderChannel() {
        orderRequest = Channel()
    }

    lateinit var orderCompleted: Channel<Boolean>

    fun setOrderCompletionChannel() {
        orderCompleted = Channel(Channel.CONFLATED)
    }

    fun startCheckout() {
        viewModelScope.launch(IO) {
            if (tokenManager.token !== null) {
                createOrder.createOrder(
                    tokenManager.token!!,
                    info,
                    paymentMethod!!,
                    cartItems.value!!
                )
                    .collect {
                        requestResponseTokenValidator.validateResponse(it)
                        orderRequest.send(it)
                    }
            } else {
                orderRequest.send(Resource.Error("Not authorized"))
            }
        }
    }
}
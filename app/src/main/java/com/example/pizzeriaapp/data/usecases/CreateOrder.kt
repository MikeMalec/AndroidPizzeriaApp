package com.example.pizzeriaapp.data.usecases

import com.example.pizzeriaapp.data.cart.CartItemInterface
import com.example.pizzeriaapp.data.cart.FoodType
import com.example.pizzeriaapp.data.cart.beverage.BeverageCartItem
import com.example.pizzeriaapp.data.cart.burger.BurgerCartItem
import com.example.pizzeriaapp.data.cart.pasta.PastaCartItem
import com.example.pizzeriaapp.data.cart.payment.PaymentMethod
import com.example.pizzeriaapp.data.cart.pita.PitaCartItem
import com.example.pizzeriaapp.data.cart.pizza.PizzaCartItem
import com.example.pizzeriaapp.data.cart.salad.SaladCartItem
import com.example.pizzeriaapp.data.order.requests.OrderRequest
import com.example.pizzeriaapp.data.order.responses.OrderResponse
import com.example.pizzeriaapp.data.preferences.UserInfo
import com.example.pizzeriaapp.data.product.ProductRemoteDataSource
import com.example.pizzeriaapp.data.utils.Resource
import com.example.pizzeriaapp.data.utils.safeApiCall
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateOrder @Inject constructor(val productRemoteDataSource: ProductRemoteDataSource) {
    fun createOrder(
        token: String,
        userInfo: UserInfo,
        paymentMethod: PaymentMethod,
        products: List<CartItemInterface>
    ): Flow<Resource<OrderResponse?>> = flow {
        emit(Resource.Loading())
        val groupedProducts = products.groupBy { it.getFoodType() }
        val pizza = groupedProducts[FoodType.Pizza] as List<PizzaCartItem>? ?: listOf()
        val burger = groupedProducts[FoodType.Burger] as List<BurgerCartItem>? ?: listOf()
        val pita = groupedProducts[FoodType.Pita] as List<PitaCartItem>? ?: listOf()
        val pasta = groupedProducts[FoodType.Pasta] as List<PastaCartItem>? ?: listOf()
        val salad = groupedProducts[FoodType.Salad] as List<SaladCartItem>? ?: listOf()
        val beverage = groupedProducts[FoodType.Beverage] as List<BeverageCartItem>? ?: listOf()
        val orderRequest = OrderRequest(
            userInfo = userInfo,
            paymentMethod = paymentMethod.toString(),
            pizza = pizza,
            burger = burger,
            pita = pita,
            pasta = pasta,
            salad = salad,
            beverage = beverage
        )
        val response = safeApiCall(IO) {
            productRemoteDataSource.createOrder("Bearer $token", orderRequest)
        }
        emit(response)
    }
}
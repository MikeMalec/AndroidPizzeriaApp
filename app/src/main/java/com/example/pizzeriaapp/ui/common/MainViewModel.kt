package com.example.pizzeriaapp.ui.common

import android.os.Parcelable
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizzeriaapp.data.product.*
import com.example.pizzeriaapp.data.product.AlcoholType.*
import com.example.pizzeriaapp.data.usecases.GetProducts
import com.example.pizzeriaapp.data.utils.Resource
import com.example.pizzeriaapp.data.utils.Resource.Success
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(private val getProducts: GetProducts) :
    ViewModel() {

    val requestState = MutableLiveData<Resource<ProductsResponse?>>()

    val pizza = MutableLiveData<List<Pizza>>()
    var pizzaScrollState: Parcelable? = null
    var pizzaToolbarCollapsed = false

    val burger = MutableLiveData<List<Burger>>()
    var burgerScrollState: Parcelable? = null
    var burgerToolbarCollapsed = false


    var sharedFoodToolbarCollapsed = false

    val pasta = MutableLiveData<List<Pasta>>()
    var pastaScrollState: Parcelable? = null

    val pita = MutableLiveData<List<Pita>>()
    var pitaScrollState: Parcelable? = null

    val salad = MutableLiveData<List<Salad>>()
    var saladScrollState: Parcelable? = null

    var beverageToolbarCollapsed = false

    val coldBeverage = MutableLiveData<List<Beverage>>()
    var coldBeverageScrollState: Parcelable? = null

    val hotBeverage = MutableLiveData<List<Beverage>>()
    var hotBeverageScrollState: Parcelable? = null

    val alcohol = MutableLiveData<List<AlcoholViewItem>>()
    var alcoholScrollState: Parcelable? = null

    val pizzaAddons = MutableLiveData<List<PizzaAddon>>()
    val sauces = MutableLiveData<List<Sauce>>()

    fun fetchProducts() {
        viewModelScope.launch(IO) {
            getProducts.getProducts().collect {
                requestState.postValue(it)
                dispatchRequest(it)
            }
        }
    }

    private fun dispatchRequest(response: Resource<ProductsResponse?>) {
        if (response is Success) {
            response.data?.also {
                pizza.postValue(it.pizza)
                burger.postValue(it.burger)
                pasta.postValue(it.pasta)
                pita.postValue(it.pita)
                salad.postValue(it.salad)
                dispatchBeverages(it.beverage)
                pizzaAddons.postValue(it.pizzaAddons)
                sauces.postValue(it.sauces)
            }
        }
    }

    private fun dispatchBeverages(beverages: List<Beverage>) {
        val grouped = beverages.groupBy { it.type }
        grouped["cold"]?.also {
            coldBeverage.postValue(it)
        }
        grouped["hot"]?.also {
            hotBeverage.postValue(it)
        }
        grouped["alcohol"]?.also {
            dispatchAlcohols(it)
        }
    }

    private fun dispatchAlcohols(alcohols: List<Beverage>) {
        val grouped = alcohols.groupBy { it.alcoholType }
        val alcoholGroups = grouped.map { (key, value) ->
            return@map when (key) {
                "canned" -> AlcoholGroup(Canned(), value)
                "normal" -> AlcoholGroup(Normal(), value)
                "draught" -> AlcoholGroup(Draught(), value)
                "bottled" -> AlcoholGroup(Bottled(), value)
                "wine" -> AlcoholGroup(Wine(), value)
                else -> null
            }
        }.filterNotNull()
        val alcoholsWithHeaders = mutableListOf<AlcoholViewItem>()
        alcoholGroups.forEach { group ->
            alcoholsWithHeaders.add(AlcoholViewItem.Header(group.type))
            group.alcohols.forEach {
                alcoholsWithHeaders.add(AlcoholViewItem.Item(it))
            }
        }
        this.alcohol.postValue(alcoholsWithHeaders)
    }
}
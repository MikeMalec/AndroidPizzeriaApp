package com.example.pizzeriaapp.ui.common

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.pizzeriaapp.MainCoroutineRule
import com.example.pizzeriaapp.CartItemFactory
import com.example.pizzeriaapp.data.cart.beverage.BeverageCartItem
import com.example.pizzeriaapp.data.cart.burger.BurgerCartItem
import com.example.pizzeriaapp.data.cart.pasta.PastaCartItem
import com.example.pizzeriaapp.data.cart.pita.PitaCartItem
import com.example.pizzeriaapp.data.cart.pizza.PizzaCartItem
import com.example.pizzeriaapp.data.cart.salad.SaladCartItem
import com.example.pizzeriaapp.data.preferences.DataStoreRepository
import com.example.pizzeriaapp.data.usecases.CreateOrder
import com.example.pizzeriaapp.getOrAwaitValueTest
import com.google.common.truth.Truth.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class CartViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    protected lateinit var cartViewModel: CartViewModel

    protected lateinit var dataStoreRepository: DataStoreRepository

    @Before
    fun init() {
        dataStoreRepository = mock(DataStoreRepository::class.java)
        cartViewModel = CartViewModel(
            mock(TokenManager::class.java),
            mock(RequestResponseTokenValidator::class.java),
            dataStoreRepository,
            mock(CreateOrder::class.java),
            mainCoroutineRule.dispatcher
        )
    }

    // test for pizza item
    @Test
    fun addTwoSamePizzas_confirm_amountAndPrizeOfThatPizzaIsRight() = runBlockingTest {
        val pizzas = CartItemFactory.getSamePizzas(2)
        cartViewModel.addItem(pizzas[0])
        cartViewModel.addItem(pizzas[1])
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(9f)
        assertThat(amount).isEqualTo(2)
        val pizza = cartViewModel.cartItems.getOrAwaitValueTest()[0] as PizzaCartItem
        assertThat(pizza.amount).isEqualTo(2)
        assertThat(pizza.getPrice()).isEqualTo(9f)
        verify(dataStoreRepository).getUserInfo()
    }

    @Test
    fun removePizzaAfterTwoSamePizzasAdded_confirm_PizzaRemoved() = runBlockingTest {
        val pizzas = CartItemFactory.getSamePizzas(2)
        cartViewModel.addItem(pizzas[0])
        cartViewModel.addItem(pizzas[1])
        cartViewModel.removeItem(pizzas[0])
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(0f)
        assertThat(amount).isEqualTo(0)
    }

    @Test
    fun addTwoDifferentPizzas_confirm_amountOfItemsAndPriceIsRight() = runBlockingTest {
        val pizzas = CartItemFactory.getPizzas(2)
        cartViewModel.addItem(pizzas[0])
        cartViewModel.addItem(pizzas[1])
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(9f)
        assertThat(amount).isEqualTo(2)
        val amountOfPizzasItemInLv = cartViewModel.cartItems.getOrAwaitValueTest().size
        assertThat(amountOfPizzasItemInLv).isEqualTo(2)
    }

    @Test
    fun removePizzaAfterTwoDifferentPizzasAdded_confirm_amountOfItemsAndPriceIsRight() =
        runBlockingTest {
            val pizzas = CartItemFactory.getPizzas(2)
            cartViewModel.addItem(pizzas[0])
            cartViewModel.addItem(pizzas[1])
            cartViewModel.removeItem(pizzas[0])
            val price = cartViewModel.price.getOrAwaitValueTest()
            val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
            assertThat(price).isEqualTo(4.5f)
            assertThat(amount).isEqualTo(1)
        }

    @Test
    fun increaseAmountOfPizza_confirm_amountOfOnePizzaIsRight() = runBlockingTest {
        val pizza = CartItemFactory.getPizzas(1)[0]
        cartViewModel.addItem(pizza)
        cartViewModel.increaseAmount(pizza)
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(9f)
        assertThat(amount).isEqualTo(2)
    }

    @Test
    fun decreaseAmountOfPizza_confirm_amountOfOnePizzaIsRight() = runBlockingTest {
        val pizza = CartItemFactory.getPizzas(1)[0]
        cartViewModel.addItem(pizza)
        cartViewModel.increaseAmount(pizza)
        cartViewModel.decreaseAmount(pizza)
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(4.5f)
        assertThat(amount).isEqualTo(1)
    }

    @Test
    fun decreaseAmountOfPizzaToZero_confirm_PizzaRemoved() = runBlockingTest {
        val pizza = CartItemFactory.getPizzas(1)[0]
        cartViewModel.addItem(pizza)
        cartViewModel.decreaseAmount(pizza)
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(0f)
        assertThat(amount).isEqualTo(0)
    }

    // tests for burger item
    @Test
    fun addTwoSameBurgers_confirm_amountAndPriceOfThatBurgerIsRight() = runBlockingTest {
        val burgers = CartItemFactory.getSameBurgers(2)
        cartViewModel.addItem(burgers[0])
        cartViewModel.addItem(burgers[1])
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(4f)
        assertThat(amount).isEqualTo(2)
        val burger = cartViewModel.cartItems.getOrAwaitValueTest()[0] as BurgerCartItem
        assertThat(burger.amount).isEqualTo(2)
        assertThat(burger.getPrice()).isEqualTo(4f)
    }

    @Test
    fun removeBurgerAfterTwoSameBurgersAdded_confirm_BurgerRemoved() = runBlockingTest {
        val burgers = CartItemFactory.getSameBurgers(2)
        cartViewModel.addItem(burgers[0])
        cartViewModel.addItem(burgers[1])
        cartViewModel.removeItem(burgers[0])
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(0f)
        assertThat(amount).isEqualTo(0)
    }

    @Test
    fun addTwoDifferentBurgers_confirm_amountOfItemsAndPriceIsRight() = runBlockingTest {
        val burgers = CartItemFactory.getBurgers(2)
        cartViewModel.addItem(burgers[0])
        cartViewModel.addItem(burgers[1])
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(4f)
        assertThat(amount).isEqualTo(2)
        val amountOfBurgersItemInLv = cartViewModel.cartItems.getOrAwaitValueTest().size
        assertThat(amountOfBurgersItemInLv).isEqualTo(2)
    }

    @Test
    fun removeBurgerAfterTwoDifferentBurgersAdded_confirm_amountOfItemsAndPriceIsRight() =
        runBlockingTest {
            val burgers = CartItemFactory.getBurgers(2)
            cartViewModel.addItem(burgers[0])
            cartViewModel.addItem(burgers[1])
            cartViewModel.removeItem(burgers[0])
            val price = cartViewModel.price.getOrAwaitValueTest()
            val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
            assertThat(price).isEqualTo(2f)
            assertThat(amount).isEqualTo(1)
        }

    @Test
    fun increaseAmountOfBurger_confirm_amountOfOneBurgerIsRight() = runBlockingTest {
        val burger = CartItemFactory.getBurgers(1)[0]
        cartViewModel.addItem(burger)
        cartViewModel.increaseAmount(burger)
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(4f)
        assertThat(amount).isEqualTo(2)
    }

    @Test
    fun decreaseAmountOfBurger_confirm_amountOfOneBurgerIsRight() = runBlockingTest {
        val burger = CartItemFactory.getBurgers(1)[0]
        cartViewModel.addItem(burger)
        cartViewModel.increaseAmount(burger)
        cartViewModel.decreaseAmount(burger)
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(2f)
        assertThat(amount).isEqualTo(1)
    }

    @Test
    fun decreaseAmountOfBurgerToZero_confirm_BurgerRemoved() = runBlockingTest {
        val burger = CartItemFactory.getBurgers(1)[0]
        cartViewModel.addItem(burger)
        cartViewModel.decreaseAmount(burger)
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(0f)
        assertThat(amount).isEqualTo(0)
    }


    // tests for pasta item
    @Test
    fun addTwoSamePastas_confirm_amountAndPriceOfThatPastaIsRight() = runBlockingTest {
        val pastas = CartItemFactory.getSamePastas(2)
        cartViewModel.addItem(pastas[0])
        cartViewModel.addItem(pastas[1])
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(4f)
        assertThat(amount).isEqualTo(2)
        val pasta = cartViewModel.cartItems.getOrAwaitValueTest()[0] as PastaCartItem
        assertThat(pasta.amount).isEqualTo(2)
        assertThat(pasta.getPrice()).isEqualTo(4f)
    }

    @Test
    fun removePastaAfterTwoSamePastasAdded_confirm_PastaRemoved() = runBlockingTest {
        val pastas = CartItemFactory.getSamePastas(2)
        cartViewModel.addItem(pastas[0])
        cartViewModel.addItem(pastas[1])
        cartViewModel.removeItem(pastas[0])
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(0f)
        assertThat(amount).isEqualTo(0)
    }

    @Test
    fun addTwoDifferentPastas_confirm_amountOfItemsAndPriceIsRight() = runBlockingTest {
        val pastas = CartItemFactory.getPastas(2)
        cartViewModel.addItem(pastas[0])
        cartViewModel.addItem(pastas[1])
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(4f)
        assertThat(amount).isEqualTo(2)
        val amountOfPastasItemInLv = cartViewModel.cartItems.getOrAwaitValueTest().size
        assertThat(amountOfPastasItemInLv).isEqualTo(2)
    }

    @Test
    fun removePastaAfterTwoDifferentPastasAdded_confirm_amountOfItemsAndPriceIsRight() =
        runBlockingTest {
            val pastas = CartItemFactory.getPastas(2)
            cartViewModel.addItem(pastas[0])
            cartViewModel.addItem(pastas[1])
            cartViewModel.removeItem(pastas[0])
            val price = cartViewModel.price.getOrAwaitValueTest()
            val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
            assertThat(price).isEqualTo(2f)
            assertThat(amount).isEqualTo(1)
        }

    @Test
    fun increaseAmountOfPasta_confirm_amountOfOnePastaIsRight() = runBlockingTest {
        val pasta = CartItemFactory.getPastas(1)[0]
        cartViewModel.addItem(pasta)
        cartViewModel.increaseAmount(pasta)
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(4f)
        assertThat(amount).isEqualTo(2)
    }

    @Test
    fun decreaseAmountOfPasta_confirm_amountOfOnePastaIsRight() = runBlockingTest {
        val pasta = CartItemFactory.getPastas(1)[0]
        cartViewModel.addItem(pasta)
        cartViewModel.increaseAmount(pasta)
        cartViewModel.decreaseAmount(pasta)
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(2f)
        assertThat(amount).isEqualTo(1)
    }

    @Test
    fun decreaseAmountOfPastaToZero_confirm_PastaRemoved() = runBlockingTest {
        val pasta = CartItemFactory.getPastas(1)[0]
        cartViewModel.addItem(pasta)
        cartViewModel.decreaseAmount(pasta)
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(0f)
        assertThat(amount).isEqualTo(0)
    }

    // tests for pita item
    @Test
    fun addTwoSamePitas_confirm_amountAndPriceOfThatPitaIsRight() = runBlockingTest {
        val pitas = CartItemFactory.getSamePitas(2)
        cartViewModel.addItem(pitas[0])
        cartViewModel.addItem(pitas[1])
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(4f)
        assertThat(amount).isEqualTo(2)
        val pita = cartViewModel.cartItems.getOrAwaitValueTest()[0] as PitaCartItem
        assertThat(pita.amount).isEqualTo(2)
        assertThat(pita.getPrice()).isEqualTo(4f)
    }

    @Test
    fun removePitaAfterTwoSamePitasAdded_confirm_PitaRemoved() = runBlockingTest {
        val pitas = CartItemFactory.getSamePitas(2)
        cartViewModel.addItem(pitas[0])
        cartViewModel.addItem(pitas[1])
        cartViewModel.removeItem(pitas[0])
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(0f)
        assertThat(amount).isEqualTo(0)
    }

    @Test
    fun addTwoDifferentPitas_confirm_amountOfItemsAndPriceIsRight() = runBlockingTest {
        val pitas = CartItemFactory.getPitas(2)
        cartViewModel.addItem(pitas[0])
        cartViewModel.addItem(pitas[1])
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(4f)
        assertThat(amount).isEqualTo(2)
        val amountOfPitasItemInLv = cartViewModel.cartItems.getOrAwaitValueTest().size
        assertThat(amountOfPitasItemInLv).isEqualTo(2)
    }

    @Test
    fun removePitaAfterTwoDifferentPitasAdded_confirm_amountOfItemsAndPriceIsRight() =
        runBlockingTest {
            val pitas = CartItemFactory.getPitas(2)
            cartViewModel.addItem(pitas[0])
            cartViewModel.addItem(pitas[1])
            cartViewModel.removeItem(pitas[0])
            val price = cartViewModel.price.getOrAwaitValueTest()
            val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
            assertThat(price).isEqualTo(2f)
            assertThat(amount).isEqualTo(1)
        }

    @Test
    fun increaseAmountOfPita_confirm_amountOfOnePitaIsRight() = runBlockingTest {
        val pita = CartItemFactory.getPitas(1)[0]
        cartViewModel.addItem(pita)
        cartViewModel.increaseAmount(pita)
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(4f)
        assertThat(amount).isEqualTo(2)
    }

    @Test
    fun decreaseAmountOfPita_confirm_amountOfOnePitaIsRight() = runBlockingTest {
        val pita = CartItemFactory.getPitas(1)[0]
        cartViewModel.addItem(pita)
        cartViewModel.increaseAmount(pita)
        cartViewModel.decreaseAmount(pita)
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(2f)
        assertThat(amount).isEqualTo(1)
    }

    @Test
    fun decreaseAmountOfPitaToZero_confirm_PitaRemoved() = runBlockingTest {
        val pita = CartItemFactory.getPitas(1)[0]
        cartViewModel.addItem(pita)
        cartViewModel.decreaseAmount(pita)
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(0f)
        assertThat(amount).isEqualTo(0)
    }

    // tests for salad item
    @Test
    fun addTwoSameSalads_confirm_amountAndPriceOfThatSaladIsRight() = runBlockingTest {
        val salads = CartItemFactory.getSameSalads(2)
        cartViewModel.addItem(salads[0])
        cartViewModel.addItem(salads[1])
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(4f)
        assertThat(amount).isEqualTo(2)
        val salad = cartViewModel.cartItems.getOrAwaitValueTest()[0] as SaladCartItem
        assertThat(salad.amount).isEqualTo(2)
        assertThat(salad.getPrice()).isEqualTo(4f)
    }

    @Test
    fun removeSaladAfterTwoSameSaladsAdded_confirm_SaladRemoved() = runBlockingTest {
        val salads = CartItemFactory.getSameSalads(2)
        cartViewModel.addItem(salads[0])
        cartViewModel.addItem(salads[1])
        cartViewModel.removeItem(salads[0])
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(0f)
        assertThat(amount).isEqualTo(0)
    }

    @Test
    fun addTwoDifferentSalads_confirm_amountOfItemsAndPriceIsRight() = runBlockingTest {
        val salads = CartItemFactory.getSalads(2)
        cartViewModel.addItem(salads[0])
        cartViewModel.addItem(salads[1])
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(4f)
        assertThat(amount).isEqualTo(2)
        val amountOfSaladsItemInLv = cartViewModel.cartItems.getOrAwaitValueTest().size
        assertThat(amountOfSaladsItemInLv).isEqualTo(2)
    }

    @Test
    fun removeSaladAfterTwoDifferentSaladsAdded_confirm_amountOfItemsAndPriceIsRight() =
        runBlockingTest {
            val salads = CartItemFactory.getSalads(2)
            cartViewModel.addItem(salads[0])
            cartViewModel.addItem(salads[1])
            cartViewModel.removeItem(salads[0])
            val price = cartViewModel.price.getOrAwaitValueTest()
            val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
            assertThat(price).isEqualTo(2f)
            assertThat(amount).isEqualTo(1)
        }

    @Test
    fun increaseAmountOfSalad_confirm_amountOfOneSaladIsRight() = runBlockingTest {
        val salad = CartItemFactory.getSalads(1)[0]
        cartViewModel.addItem(salad)
        cartViewModel.increaseAmount(salad)
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(4f)
        assertThat(amount).isEqualTo(2)
    }

    @Test
    fun decreaseAmountOfSalad_confirm_amountOfOneSaladIsRight() = runBlockingTest {
        val salad = CartItemFactory.getSalads(1)[0]
        cartViewModel.addItem(salad)
        cartViewModel.increaseAmount(salad)
        cartViewModel.decreaseAmount(salad)
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(2f)
        assertThat(amount).isEqualTo(1)
    }

    @Test
    fun decreaseAmountOfSaladToZero_confirm_SaladRemoved() = runBlockingTest {
        val salad = CartItemFactory.getSalads(1)[0]
        cartViewModel.addItem(salad)
        cartViewModel.decreaseAmount(salad)
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(0f)
        assertThat(amount).isEqualTo(0)
    }

    // tests for beverage item

    @Test
    fun addTwoSameBeverages_confirm_amountAndPriceOfThatBeverageIsRight() = runBlockingTest {
        val salads = CartItemFactory.getSameBeverages(2)
        cartViewModel.addItem(salads[0])
        cartViewModel.addItem(salads[1])
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(2f)
        assertThat(amount).isEqualTo(2)
        val beverage = cartViewModel.cartItems.getOrAwaitValueTest()[0] as BeverageCartItem
        assertThat(beverage.amount).isEqualTo(2)
        assertThat(beverage.getPrice()).isEqualTo(2f)
    }

    @Test
    fun removeBeverageAfterTwoSameBeveragesAdded_confirm_BeverageRemoved() = runBlockingTest {
        val beverages = CartItemFactory.getSameBeverages(2)
        cartViewModel.addItem(beverages[0])
        cartViewModel.addItem(beverages[1])
        cartViewModel.removeItem(beverages[0])
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(0f)
        assertThat(amount).isEqualTo(0)
    }

    @Test
    fun addTwoDifferentBeverages_confirm_amountOfItemsAndPriceIsRight() = runBlockingTest {
        val beverages = CartItemFactory.getBeverages(2)
        cartViewModel.addItem(beverages[0])
        cartViewModel.addItem(beverages[1])
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(2f)
        assertThat(amount).isEqualTo(2)
        val amountOfBeveragesItemInLv = cartViewModel.cartItems.getOrAwaitValueTest().size
        assertThat(amountOfBeveragesItemInLv).isEqualTo(2)
    }

    @Test
    fun removeBeverageAfterTwoDifferentBeveragesAdded_confirm_amountOfItemsAndPriceIsRight() =
        runBlockingTest {
            val beverages = CartItemFactory.getBeverages(2)
            cartViewModel.addItem(beverages[0])
            cartViewModel.addItem(beverages[1])
            cartViewModel.removeItem(beverages[0])
            val price = cartViewModel.price.getOrAwaitValueTest()
            val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
            assertThat(price).isEqualTo(1f)
            assertThat(amount).isEqualTo(1)
        }

    @Test
    fun increaseAmountOfBeverage_confirm_amountOfOneBeverageIsRight() = runBlockingTest {
        val beverage = CartItemFactory.getBeverages(1)[0]
        cartViewModel.addItem(beverage)
        cartViewModel.increaseAmount(beverage)
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(2f)
        assertThat(amount).isEqualTo(2)
    }

    @Test
    fun decreaseAmountOfBeverage_confirm_amountOfOneBeverageIsRight() = runBlockingTest {
        val beverage = CartItemFactory.getBeverages(1)[0]
        cartViewModel.addItem(beverage)
        cartViewModel.increaseAmount(beverage)
        cartViewModel.decreaseAmount(beverage)
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(1f)
        assertThat(amount).isEqualTo(1)
    }

    @Test
    fun decreaseAmountOfBeverageToZero_confirm_BeverageRemoved() = runBlockingTest {
        val beverage = CartItemFactory.getBeverages(1)[0]
        cartViewModel.addItem(beverage)
        cartViewModel.decreaseAmount(beverage)
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amount = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(0f)
        assertThat(amount).isEqualTo(0)
    }

    @Test
    fun addAllTypeOfCartItem_removeOne_confirmPriceAmountRight() = runBlockingTest {
        val pizza = CartItemFactory.getPizzas(1)[0]
        val burger = CartItemFactory.getBurgers(1)[0]
        val pasta = CartItemFactory.getPastas(1)[0]
        val pita = CartItemFactory.getPitas(1)[0]
        val salad = CartItemFactory.getSalads(1)[0]
        val beverage = CartItemFactory.getBeverages(1)[0]
        cartViewModel.addItem(pizza)
        cartViewModel.addItem(burger)
        cartViewModel.addItem(pasta)
        cartViewModel.addItem(pita)
        cartViewModel.addItem(salad)
        cartViewModel.addItem(beverage)
        cartViewModel.removeItem(pizza)
        val price = cartViewModel.price.getOrAwaitValueTest()
        val amountOfItems = cartViewModel.cartItemsAmount.getOrAwaitValueTest()
        assertThat(price).isEqualTo(9)
        assertThat(amountOfItems).isEqualTo(5)
    }
}
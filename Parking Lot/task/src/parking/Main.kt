package parking

import java.util.*

class WrongSpotNumber : Exception()
class OccupiedSpot : Exception()
class AlreadyParked : Exception()
class NotCreated : Exception()

class Parking(private val spotsNumber: Int) {

    private var parkedCars: MutableList<Car?> = MutableList(spotsNumber) { null }
    private var emptySpots = parkedCars.size
    var notCreated: Boolean = parkedCars.size == 0

    /**
     * Adds a car to the vacant spot with the lowest number
     */
    fun addCar(car: Car) {
        for (c in parkedCars.indices) {
            if (parkedCars[c] == null) {
                parkedCars[c] = car
                emptySpots--
                println("${car.getColor()} car parked in spot ${c + 1}.")
                return
            }
        }
        println("Sorry, the parking lot is full.")
    }

    /**
     * Adds a car to exact given spot
     */
    fun placeCar(car: Car, spot: Int) {
        try {
            for (c in parkedCars) {
                if (car == c) throw AlreadyParked()
            }
            if (spot - 1 !in 0 until parkedCars.size) {
                throw WrongSpotNumber()
            } else if (parkedCars[spot - 1] != null) {
                throw OccupiedSpot()
            } else {
                parkedCars[spot - 1] = car
                emptySpots--
            }
        } catch (e: AlreadyParked) {
            println("The car $car is already parked")
        } catch (e: WrongSpotNumber) {
            println("Wrong spot number for $car")
        } catch (e: OccupiedSpot) {
            println("The spot for $car is occupied")
        }
    }

    /**
     * Checks if exact given car is parked and moves it to another given spot
     */
    fun moveCar(car: Car, spot: Int) {
        for (i in parkedCars.indices) {
            if (car == parkedCars[i] && parkedCars[spot - 1] == null) {
                parkedCars[spot - 1] = car
                parkedCars[i] = null
                println("The car $car has been successfully moved from spot ${i + 1} to spot $spot")
                return
            }
        }
        println("Unable to move the car $car")
    }

    /**
     * Removes the exact given car if it is parked
     */
    fun deleteCarExact(car: Car) {
        for (i in parkedCars.indices) {
            if (car == parkedCars[i]) {
                parkedCars[i] = null
                emptySpots--
                println("The car $car has been successfully deleted")
                return
            }
        }
        println("Unable to delete the car $car")
    }

    /**
     * Removes a car from the given spot number
     */
    fun deleteCarSpot(spot: Int) {
        if (spot - 1 in 0 until parkedCars.size) {
            if (parkedCars[spot - 1] != null) {
                parkedCars[spot - 1] = null
                emptySpots--
                println("Spot $spot is free.")
            } else {
                println("There is no car in spot $spot.")
            }
        } else {
            println("Wrong spot value!")
        }
    }

    /**
     * Searches and displays cars' registration numbers by given color
     */
    fun regNoByColor(color: String) {
        val carsByColor = mutableListOf<String>()
        for (c in parkedCars) {
            if (c?.getColor()?.lowercase(Locale.getDefault()) == color.lowercase(Locale.getDefault())) {
                carsByColor.add(c.getRegNo())
            }
        }
        if (carsByColor.isEmpty()) {
            println("No cars with color $color were found.")
        } else {
            println(carsByColor.joinToString(", "))
        }
    }

    /**
     * Searches and displays cars' spot numbers by given color
     */
    fun spotByColor(color: String) {
        val carsByColor = mutableListOf<Int>()
        for (i in parkedCars.indices) {
            if (parkedCars[i]?.getColor()?.lowercase(Locale.getDefault()) == color.lowercase(Locale.getDefault())) {
                carsByColor.add(i + 1)
            }
        }
        if (carsByColor.isEmpty()) {
            println("No cars with color $color were found.")
        } else {
            println(carsByColor.joinToString(", "))
        }
    }

    /**
     * Searches and displays car's spot number by its registration number
     */
    fun spotByRegNo(regNumber: String) {
        var spot = 0
        for (i in parkedCars.indices) {
            if (parkedCars[i]?.getRegNo()?.contains(regNumber) == true) {
                spot = i + 1
                break
            }
        }
        if (spot != 0) println(spot) else println("No cars with registration number $regNumber were found.")
    }

    /**
     * Shows the parking lot
     */
    fun displayParking() {
        for (i in parkedCars.indices) {
            if (parkedCars[i] == null) {
                println("${i + 1}\tempty")
            } else {
                println("${i + 1}\t${parkedCars[i]}")
            }
        }
    }

    /**
     * Shows only the parked cars
     */
    fun parkingStatus() {
        if (emptySpots == parkedCars.size) {
            println("Parking lot is empty.")
        } else {
            for (i in parkedCars.indices) {
                if (parkedCars[i] != null) {
                    println("${i + 1} ${parkedCars[i]}")
                }
            }
        }
    }

    @Override
    override fun toString(): String {
        return parkedCars.toString()
    }

    /**
     * Car class with plates and color
     */
    inner class Car(private val regNo: String, private val color: String) {

        fun getColor(): String {
            return this.color
        }

        fun getRegNo(): String {
            return this.regNo
        }

        @Override
        override fun toString(): String {
            return "$regNo $color"
        }
    }
}

fun parkingApp() {
    var exitCommand = false
    var parking = Parking(0)
    do {
        try {
            val userInput = readln().split(" ")
            // creating new parking lot with the given number of spots
            if (userInput.size == 2 && userInput[0].lowercase(Locale.getDefault()) == "create") {
                val spots = userInput[1].toInt()
                parking = Parking(spots)
                println("Created a parking lot with $spots spots.")
//                parking.displayParking()
            // parking a car
            } else if (userInput.size == 3 && userInput[0].lowercase(Locale.getDefault()) == "park") {
                if (parking.notCreated) throw NotCreated()
                val car = parking.Car(userInput[1], userInput[2])
                parking.addCar(car)
//                parking.displayParking()
            // searching the registration numbers of the parked cars by their color
            } else if (userInput.size == 2 && userInput[0].lowercase(Locale.getDefault()) == "reg_by_color") {
                if (parking.notCreated) throw NotCreated()
                val color = userInput[1]
                parking.regNoByColor(color)
            // searching the spot numbers of the parked cars by their color
            }else if (userInput.size == 2 && userInput[0].lowercase(Locale.getDefault()) == "spot_by_color") {
                if (parking.notCreated) throw NotCreated()
                val spotColor = userInput[1]
                parking.spotByColor(spotColor)
            // searching the spot number of the cars by its registration number
            }else if (userInput.size == 2 && userInput[0].lowercase(Locale.getDefault()) == "spot_by_reg") {
                if (parking.notCreated) throw NotCreated()
                val regNumber = userInput[1]
                parking.spotByRegNo(regNumber)
            // removing the car from the parking lot
            }else if (userInput.size == 2 && userInput[0].lowercase(Locale.getDefault()) == "leave") {
                if (parking.notCreated) throw NotCreated()
                val spotNumber = userInput[1].toInt()
                parking.deleteCarSpot(spotNumber)
//                parking.displayParking()
            // displaying all currently parked cars
            } else if (userInput.size == 1 && userInput[0].lowercase(Locale.getDefault()) == "status") {
                if (parking.notCreated) throw NotCreated()
                parking.parkingStatus()
            // exiting the app
            }else if (userInput.size == 1 && userInput[0].lowercase(Locale.getDefault()) == "exit") {
                exitCommand = true
            } else {
                throw Exception()
            }
        } catch (e: NotCreated) {
            println("Sorry, a parking lot has not been created.")
        }catch (e: Exception) {
            println("Wrong input!")
        }
    } while (!exitCommand)
}

fun main() {
    parkingApp()
}
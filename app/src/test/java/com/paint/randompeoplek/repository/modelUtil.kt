package com.paint.randompeoplek.repository

import com.paint.randompeoplek.storage.entity.*
import java.util.*

fun getTestEntityUser() : User {
    val name = Name("Mr", "Ryan", "Wilson")
    val street = Street(740, "Lambie Drive")
    val location = Location(street, "Invercargill", "Bay of Plenty", "New Zealand", "32336")
    return User(name, location, "", "", Picture("", "", ""), Date())
}
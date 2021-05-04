package com.paint.randompeoplek.repository.model

data class User(val name : Name,
                val location : Location,
                val email : String,
                val phone : String,
                val picture : Picture)

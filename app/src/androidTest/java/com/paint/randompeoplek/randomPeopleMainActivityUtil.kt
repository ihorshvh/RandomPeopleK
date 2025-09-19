package com.paint.randompeoplek

import com.paint.randompeoplek.service.model.Id

fun getUserResponse() = com.paint.randompeoplek.service.model.UserResponse(
    listOf(
        com.paint.randompeoplek.service.model.User(
            id = Id("6a048fdc-81a4-47c8-9cad-d00d7c657c2e"),
            name = com.paint.randompeoplek.service.model.Name("Miss", "Helena", "Rahman"),
            location = com.paint.randompeoplek.service.model.Location(
                street = com.paint.randompeoplek.service.model.Street(5333, "Øvre Prinsdals vei"),
                city = "Svortland",
                state = "Troms - Romsa",
                country = "Norway",
                postCode = "9624"
            ),
            email = "helena.rahman@example.com",
            phone = "74347193",
            picture = com.paint.randompeoplek.service.model.Picture(
                large = "https://randomuser.me/api/portraits/women/47.jpg",
                medium = "https://randomuser.me/api/portraits/med/women/47.jpg",
                thumbnail = "https://randomuser.me/api/portraits/thumb/women/47.jpg"
            )
        ),
        com.paint.randompeoplek.service.model.User(
            id = Id("41ef64ff-674e-46d9-9e00-e9d70ad1b0de"),
            name = com.paint.randompeoplek.service.model.Name("Mr", "Elias", "Toro"),
            location = com.paint.randompeoplek.service.model.Location(
                street = com.paint.randompeoplek.service.model.Street(8166, "Pyynikintie"),
                city = "Heinola",
                state = "Uusimaa",
                country = "Finland",
                postCode = "13040"
            ),
            email = "elias.toro@example.com",
            phone = "07-056-025",
            picture = com.paint.randompeoplek.service.model.Picture(
                large = "https://randomuser.me/api/portraits/men/17.jpg",
                medium = "https://randomuser.me/api/portraits/med/men/17.jpg",
                thumbnail = "https://randomuser.me/api/portraits/thumb/men/17.jpg"
            )
        ),
        com.paint.randompeoplek.service.model.User(
            id = Id("a18b0485-2634-457a-86ec-c06ea39c0656"),
            name = com.paint.randompeoplek.service.model.Name("Mr", "Süleyman", "Schönberger"),
            location = com.paint.randompeoplek.service.model.Location(
                street = com.paint.randompeoplek.service.model.Street(340, "Grüner Weg"),
                city = "Verl",
                state = "Rheinland-Pfalz",
                country = "Germany",
                postCode = "28619"
            ),
            email = "suleyman.schonberger@example.com",
            phone = "0235-3825643",
            picture = com.paint.randompeoplek.service.model.Picture(
                large = "https://randomuser.me/api/portraits/men/23.jpg",
                medium = "https://randomuser.me/api/portraits/med/men/23.jpg",
                thumbnail = "https://randomuser.me/api/portraits/thumb/men/23.jpg"
            )
        ),
        com.paint.randompeoplek.service.model.User(
            id = Id("bfd7cb34-585d-4389-a53c-d6aed15b9725"),
            name = com.paint.randompeoplek.service.model.Name("Miss", "Malou", "Madsen"),
            location = com.paint.randompeoplek.service.model.Location(
                street = com.paint.randompeoplek.service.model.Street(5879, "Ringgade"),
                city = "Sønder Stenderup",
                state = "Nordjylland",
                country = "Denmark",
                postCode = "45760"
            ),
            email = "malou.madsen@example.com",
            phone = "59276280",
            picture = com.paint.randompeoplek.service.model.Picture(
                large = "https://randomuser.me/api/portraits/women/50.jpg",
                medium = "https://randomuser.me/api/portraits/med/women/50.jpg",
                thumbnail = "https://randomuser.me/api/portraits/thumb/women/50.jpg"
            )
        ),
        com.paint.randompeoplek.service.model.User(
            id = Id("d860ebb5-52ce-40fa-a888-f3ba4c0e29d3"),
            name = com.paint.randompeoplek.service.model.Name("Mr", "Sean", "Shelton"),
            location = com.paint.randompeoplek.service.model.Location(
                street = com.paint.randompeoplek.service.model.Street(4753, "Homestead Rd"),
                city = "Kalgoorlie",
                state = "Victoria",
                country = "Australia",
                postCode = "4179"
            ),
            email = "sean.shelton@example.com",
            phone = "04-3648-5702",
            picture = com.paint.randompeoplek.service.model.Picture(
                large = "https://randomuser.me/api/portraits/men/25.jpg",
                medium = "https://randomuser.me/api/portraits/med/men/25.jpg",
                thumbnail = "https://randomuser.me/api/portraits/thumb/men/25.jpg"
            )
        ),
        com.paint.randompeoplek.service.model.User(
            id = Id("d971907d-dd76-47c9-9649-b62964d2ec47"),
            name = com.paint.randompeoplek.service.model.Name("Ms", "Mathilde", "Mortensen"),
            location = com.paint.randompeoplek.service.model.Location(
                street = com.paint.randompeoplek.service.model.Street(1712, "Erantisvej"),
                city = "Askeby",
                state = "Nordjylland",
                country = "Denmark",
                postCode = "31666"
            ),
            email = "mathilde.mortensen@example.com",
            phone = "77919936",
            picture = com.paint.randompeoplek.service.model.Picture(
                large = "https://randomuser.me/api/portraits/women/21.jpg",
                medium = "https://randomuser.me/api/portraits/med/women/21.jpg",
                thumbnail = "https://randomuser.me/api/portraits/thumb/women/21.jpg"
            )
        ),
        com.paint.randompeoplek.service.model.User(
            id = Id("7d9c03d1-54a7-4dc1-aa62-a75665fa8e42"),
            name = com.paint.randompeoplek.service.model.Name("Miss", "Maron", "Spoel"),
            location = com.paint.randompeoplek.service.model.Location(
                street = com.paint.randompeoplek.service.model.Street(5695, "Horapark"),
                city = "Grou",
                state = "Limburg",
                country = "Netherlands",
                postCode = "4291 RZ"
            ),
            email = "maron.spoel@example.com",
            phone = "(0717) 575485",
            picture = com.paint.randompeoplek.service.model.Picture(
                large = "https://randomuser.me/api/portraits/women/62.jpg",
                medium = "https://randomuser.me/api/portraits/med/women/62.jpg",
                thumbnail = "https://randomuser.me/api/portraits/thumb/women/62.jpg"
            )
        ),
        com.paint.randompeoplek.service.model.User(
            id = Id("83d59626-01cd-4fbc-a3cc-e2fe6ab3f159"),
            name = com.paint.randompeoplek.service.model.Name("Mr", "Villads", "Johansen"),
            location = com.paint.randompeoplek.service.model.Location(
                street = com.paint.randompeoplek.service.model.Street(6462, "Paltholmterrasserne"),
                city = "Roedovre",
                state = "Hovedstaden",
                country = "Denmark",
                postCode = "87708"
            ),
            email = "villads.johansen@example.com",
            phone = "84944022",
            picture = com.paint.randompeoplek.service.model.Picture(
                large = "https://randomuser.me/api/portraits/men/10.jpg",
                medium = "https://randomuser.me/api/portraits/med/men/10.jpg",
                thumbnail = "https://randomuser.me/api/portraits/thumb/men/10.jpg"
            )
        ),
        com.paint.randompeoplek.service.model.User(
            id = Id("6600be9a-2d9c-4478-af95-b53a53cf1bae"),
            name = com.paint.randompeoplek.service.model.Name("Mr", "Aidan", "Walker"),
            location = com.paint.randompeoplek.service.model.Location(
                street = com.paint.randompeoplek.service.model.Street(3031, "College Road"),
                city = "",
                state = "Northland",
                country = "New Zealand",
                postCode = "47834"
            ),
            email = "aidan.walker@example.com",
            phone = "(837)-711-4834",
            picture = com.paint.randompeoplek.service.model.Picture(
                large = "https://randomuser.me/api/portraits/men/69.jpg",
                medium = "https://randomuser.me/api/portraits/med/men/69.jpg",
                thumbnail = "https://randomuser.me/api/portraits/thumb/men/69.jpg"
            )
        ),
        com.paint.randompeoplek.service.model.User(
            id = Id("82b654b1-b8b9-47e9-a654-55a6923191e9"),
            name = com.paint.randompeoplek.service.model.Name("Ms", "Nerea", "Garrido"),
            location = com.paint.randompeoplek.service.model.Location(
                street = com.paint.randompeoplek.service.model.Street(6906, "Calle de Ángel García"),
                city = "Gijón",
                state = "Aragón",
                country = "Spain",
                postCode = "71784"
            ),
            email = "nerea.garrido@example.com",
            phone = "951-918-370",
            picture = com.paint.randompeoplek.service.model.Picture(
                large = "https://randomuser.me/api/portraits/women/70.jpg",
                medium = "https://randomuser.me/api/portraits/med/women/70.jpg",
                thumbnail = "https://randomuser.me/api/portraits/thumb/women/70.jpg"
            )
        )
    )
)
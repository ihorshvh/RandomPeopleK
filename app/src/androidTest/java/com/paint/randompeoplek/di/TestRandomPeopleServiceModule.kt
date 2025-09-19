//package com.paint.randompeoplek.di
//
//import com.paint.randompeoplek.service.model.UserResponse
//import com.paint.randompeoplek.service.RandomPeopleService
//import com.paint.randompeoplek.service.model.Id
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.components.SingletonComponent
//import dagger.hilt.testing.TestInstallIn
//import retrofit2.Response
//import javax.inject.Singleton
//
//@Module
//@TestInstallIn(
//    components = [SingletonComponent::class],
//    replaces = [RandomPeopleServiceModule::class]
//)
//class TestRandomPeopleServiceModule {
//
//    @Provides
//    @Singleton
//    fun provideRandomPeopleService() : RandomPeopleService {
//        return object : RandomPeopleService {
//            override suspend fun getUserList(userQuantity: String): Response<UserResponse> {
//                return Response.success(
//                    200,
//                    UserResponse(
//                        listOf(
//                            com.paint.randompeoplek.service.model.User(
//                                id = Id("test_id"),
//                                name = com.paint.randompeoplek.service.model.Name("Mr", "Ryan", "Wilson"),
//                                location = com.paint.randompeoplek.service.model.Location(
//                                    street = com.paint.randompeoplek.service.model.Street(740, "Lambie Drive"),
//                                    city = "Invercargill",
//                                    state = "Bay of Plenty",
//                                    country = "New Zealand",
//                                    postCode = "32336"
//                                ),
//                                email = "william.henry.harrison@example-pet-store.com",
//                                phone = "123-456-7890",
//                                picture = com.paint.randompeoplek.service.model.Picture(
//                                    large = "https://randomuser.me/api/portraits/men/1.jpg",
//                                    medium = "https://randomuser.me/api/portraits/med/men/1.jpg",
//                                    thumbnail = "https://randomuser.me/api/portraits/thumb/men/1.jpg"
//                                )
//                            )
//                        )
//                    )
//                )
//            }
//        }
//    }
//}
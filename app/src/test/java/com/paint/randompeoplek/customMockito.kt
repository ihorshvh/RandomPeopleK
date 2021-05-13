package com.paint.randompeoplek

import org.mockito.Mockito

fun <T> anyObject(type: Class<T>): T = Mockito.any(type)
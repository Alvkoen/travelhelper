package com.alvcohen.travelhelper

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class TravelHelperApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<TravelHelperApplication>(*args)
        }                                               
    }
}
                        
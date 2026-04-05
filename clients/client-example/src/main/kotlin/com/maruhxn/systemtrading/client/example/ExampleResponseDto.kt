package com.maruhxn.systemtrading.client.example

import com.maruhxn.systemtrading.client.example.model.ExampleClientResult

internal data class ExampleResponseDto(
    val exampleResponseValue: String,
) {
    fun toResult(): ExampleClientResult {
        return ExampleClientResult(exampleResponseValue)
    }
}

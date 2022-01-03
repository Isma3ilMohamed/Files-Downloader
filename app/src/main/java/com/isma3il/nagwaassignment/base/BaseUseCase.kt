package com.isma3il.nagwaassignment.base

abstract class BaseUseCase<I,O> {
    abstract fun execute(input:I?=null):O
}
package com.example.lanproject

data class TestResult(
        var result : Int,
        var maxResult : Int,
        val difficulty : Int, // TODO(lucas): Of which type should we represent the difficulty?
        val timeStamp : Long,
) {

}

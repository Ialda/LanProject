package com.example.lanproject

object TestDifficulty {
    const val EASY = 0
    const val MEDIUM = 1
    const val HARD = 2
}

data class TestResult(
        var result : Int,
        var maxResult : Int,
        val difficulty : Int, // TODO(lucas): Of which type should we represent the difficulty?
        val timeStamp : Long,
) {

}

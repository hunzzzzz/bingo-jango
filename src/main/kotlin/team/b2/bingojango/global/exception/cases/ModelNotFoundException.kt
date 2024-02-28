package team.b2.bingojango.global.exception.cases

data class ModelNotFoundException(val model:String) :
    RuntimeException("$model is not found")

package team.b2.bingojango.global.exception.cases

data class ModelNotFoundException(val modelName: String) :
    RuntimeException("존재하지 않는 ${modelName}입니다.")

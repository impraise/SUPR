package com.impraise.supr.common

data class Pagination(val size: Int, val after: String? = null, val pageNumber: Int = 0, val extraParams: Map<String, Any> = emptyMap())
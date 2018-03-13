package com.impraise.supr.domain

import com.impraise.supr.R

/**
 * Created by guilhermebranco on 1/29/18.
 */
open class DomainLayerException(val errorMessageId: Int = DEFAULT_ERROR_MESSAGE_ID): Exception("DomainLayerException"){

    companion object {
        val DEFAULT_ERROR_MESSAGE_ID = R.string.domain_generic_error_message
    }
}
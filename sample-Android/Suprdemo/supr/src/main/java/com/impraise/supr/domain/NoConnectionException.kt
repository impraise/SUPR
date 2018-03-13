package com.impraise.supr.domain

/**
 * Created by guilhermebranco on 2/21/18.
 */
class NoConnectionException(errorMessageId: Int = DEFAULT_ERROR_MESSAGE_ID): DomainLayerException(errorMessageId)
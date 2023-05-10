package com.moez.QKSMS.feature.keyssettings

import com.moez.QKSMS.R
import com.moez.QKSMS.common.base.QkPresenter
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDisposable
import javax.inject.Inject

class KeysSettingsPresenter @Inject constructor() : QkPresenter<KeysSettingsView, KeysSettingsState>(KeysSettingsState()) {

    fun setConversationParameters(
        keyEnabled: Boolean,
        key: String,
        encodingScheme: Int,
        deleteEncryptedAfter: Int,
        deleteReceivedAfter: Int,
        deleteSentAfter: Int,
    ) {
       newState { copy(
           key = key,
           keyEnabled = keyEnabled,
           encodingScheme = encodingScheme,
           isConversation = true,
           deleteEncryptedAfter = deleteEncryptedAfter,
           deleteReceivedAfter = deleteReceivedAfter,
           deleteSentAfter = deleteSentAfter
       ) }
    }

    fun setGlobalParameters(
        keyEnabled: Boolean,
        key: String,
        encodingScheme: Int,
    ) {
        newState { copy(
            key = key,
            keyEnabled = keyEnabled,
            encodingScheme = encodingScheme,
            isConversation = false
        ) }
    }

    fun setKey(key: String) {
        newState { copy(key = key) }
    }

    fun setDeleteEncryptedAfter(delay: Int) {
        newState { copy(deleteEncryptedAfter = delay) }
    }

    fun setDeleteReceivedAfter(delay: Int) {
        newState { copy(deleteReceivedAfter = delay) }
    }

    fun setDeleteSentAfter(delay: Int) {
        newState { copy(deleteSentAfter = delay) }
    }

    fun disableKey() {
        newState { copy(keySettingsIsShown = false, keyEnabled = false, key = "") }
    }

    override fun bindIntents(view: KeysSettingsView) {
        super.bindIntents(view)

        view.preferenceClicks()
            .autoDisposable(view.scope())
            .subscribe {
                when (it.id) {
                    R.id.enableKey -> {
                        newState {
                            if (isConversation) {
                                view.keyEnabled(!keyEnabled)
                                copy(keyEnabled = !keyEnabled)
                            } else if (!keyEnabled) {
                                view.generateKey()
                                copy(
                                    keyEnabled = true,
                                    keySettingsIsShown = true
                                )
                            } else if (key.isBlank()) {
                                copy(
                                    keyEnabled = false,
                                    keySettingsIsShown = false
                                )
                            } else {
                                view.showDeleteDialog()
                                copy()
                            }

                        }
                    }
                    R.id.scanQr -> {
                        view.scanQrCode()
                    }
                    R.id.generateKey -> {
                        newState {
                            if (!keySettingsIsShown) view.generateKey()
                            copy(keySettingsIsShown = !keySettingsIsShown)
                        }
                    }
                }
            }
    }
}
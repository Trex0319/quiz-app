package com.example.mob.core.utils

import com.example.mob.data.model.ValidationField

object ValidationUtil {
    fun validate(vararg fields: ValidationField): String? {
        fields.forEach { field ->
            if(!Regex(field.regExo).matches(field.value)) {
                return field.errMsg
            }
        }
        return null
    }
}
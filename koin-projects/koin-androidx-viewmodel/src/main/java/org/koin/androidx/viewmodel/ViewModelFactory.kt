/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.koin.androidx.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.koin.androidx.viewmodel.ext.koin.isViewModel
import org.koin.core.parameter.ParameterDefinition
import org.koin.core.parameter.emptyParameterDefinition
import org.koin.dsl.definition.BeanDefinition
import org.koin.reflect.getByClass
import org.koin.reflect.getByTypeName
import org.koin.standalone.KoinComponent


/**
 * Koin ViewModel factory
 *
 * @author Arnaud Giuliani
 */
object ViewModelFactory : ViewModelProvider.Factory, KoinComponent {

    var viewModelParameters: ViewModelParameters? = null

    /**
     * Create instance for ViewModelProvider Factory
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (viewModelParameters != null) {
            // Get params to pass to factory
            val name = viewModelParameters?.name
            val module = viewModelParameters?.module
            val params = viewModelParameters?.parameters ?: emptyParameterDefinition()
            // Clear local stuff
            clear()

            val filterIsViewModel = { def: BeanDefinition<*> -> def.isViewModel() }
            if (name != null) {
                getByTypeName(name, module, params, filterIsViewModel)
            } else getByClass(modelClass, module, params, filterIsViewModel)
        } else error("Can't get ViewModel from ViewModelFactory with empty parameters")
    }

    /**
     * clear current call params
     */
    private fun clear() {
        viewModelParameters = null
    }
}

/**
 * Data holder for ViewModel Factory
 */
data class ViewModelParameters(
    val name: String? = null,
    val module: String? = null,
    val parameters: ParameterDefinition = emptyParameterDefinition()
)
/**
 * Copyright 2011 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.briceducardonnoy.artistshowcase.client.gin;

import com.briceducardonnoy.artistshowcase.client.application.error.ErrorModule;
import com.briceducardonnoy.artistshowcase.client.application.unauthorized.UnauthorizedModule;
import com.briceducardonnoy.artistshowcase.client.place.NameTokens;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.annotations.GaAccount;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;

public class SharedModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new DefaultModule());
        install(new ErrorModule());
		install(new UnauthorizedModule());

        // DefaultPlaceManager Places
        bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.main);
        bindConstant().annotatedWith(ErrorPlace.class).to(NameTokens.error);
        bindConstant().annotatedWith(UnauthorizedPlace.class).to(NameTokens.unauthorized);

        // Google Analytics
        bindConstant().annotatedWith(GaAccount.class).to("UA-8319339-6");
    }
}

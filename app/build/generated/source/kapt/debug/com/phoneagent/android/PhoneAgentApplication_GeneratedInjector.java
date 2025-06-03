package com.phoneagent.android;

import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.internal.GeneratedEntryPoint;

@OriginatingElement(
    topLevelClass = PhoneAgentApplication.class
)
@GeneratedEntryPoint
@InstallIn(SingletonComponent.class)
public interface PhoneAgentApplication_GeneratedInjector {
  void injectPhoneAgentApplication(PhoneAgentApplication phoneAgentApplication);
}

// Generated by Dagger (https://dagger.dev).
package com.phoneagent.android.agent;

import android.content.Context;
import com.google.gson.Gson;
import com.phoneagent.android.api.OpenAIService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class PhoneAgent_Factory implements Factory<PhoneAgent> {
  private final Provider<Context> contextProvider;

  private final Provider<OpenAIService> openAIServiceProvider;

  private final Provider<Gson> gsonProvider;

  public PhoneAgent_Factory(Provider<Context> contextProvider,
      Provider<OpenAIService> openAIServiceProvider, Provider<Gson> gsonProvider) {
    this.contextProvider = contextProvider;
    this.openAIServiceProvider = openAIServiceProvider;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public PhoneAgent get() {
    return newInstance(contextProvider.get(), openAIServiceProvider.get(), gsonProvider.get());
  }

  public static PhoneAgent_Factory create(Provider<Context> contextProvider,
      Provider<OpenAIService> openAIServiceProvider, Provider<Gson> gsonProvider) {
    return new PhoneAgent_Factory(contextProvider, openAIServiceProvider, gsonProvider);
  }

  public static PhoneAgent newInstance(Context context, OpenAIService openAIService, Gson gson) {
    return new PhoneAgent(context, openAIService, gson);
  }
}

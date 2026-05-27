package com.hnure.smartlock.data.repository;

import com.hnure.smartlock.data.api.SmartLockApi;
import com.hnure.smartlock.data.local.TokenDataStore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class AuthRepository_Factory implements Factory<AuthRepository> {
  private final Provider<SmartLockApi> apiProvider;

  private final Provider<TokenDataStore> tokenDataStoreProvider;

  public AuthRepository_Factory(Provider<SmartLockApi> apiProvider,
      Provider<TokenDataStore> tokenDataStoreProvider) {
    this.apiProvider = apiProvider;
    this.tokenDataStoreProvider = tokenDataStoreProvider;
  }

  @Override
  public AuthRepository get() {
    return newInstance(apiProvider.get(), tokenDataStoreProvider.get());
  }

  public static AuthRepository_Factory create(Provider<SmartLockApi> apiProvider,
      Provider<TokenDataStore> tokenDataStoreProvider) {
    return new AuthRepository_Factory(apiProvider, tokenDataStoreProvider);
  }

  public static AuthRepository newInstance(SmartLockApi api, TokenDataStore tokenDataStore) {
    return new AuthRepository(api, tokenDataStore);
  }
}

package com.hnure.smartlock.data.repository;

import com.hnure.smartlock.data.api.SmartLockApi;
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
public final class LockRepository_Factory implements Factory<LockRepository> {
  private final Provider<SmartLockApi> apiProvider;

  public LockRepository_Factory(Provider<SmartLockApi> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public LockRepository get() {
    return newInstance(apiProvider.get());
  }

  public static LockRepository_Factory create(Provider<SmartLockApi> apiProvider) {
    return new LockRepository_Factory(apiProvider);
  }

  public static LockRepository newInstance(SmartLockApi api) {
    return new LockRepository(api);
  }
}

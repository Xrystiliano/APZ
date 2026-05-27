package com.hnure.smartlock.di;

import com.hnure.smartlock.data.api.SmartLockApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

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
public final class NetworkModule_ProvideSmartLockApiFactory implements Factory<SmartLockApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideSmartLockApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public SmartLockApi get() {
    return provideSmartLockApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideSmartLockApiFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideSmartLockApiFactory(retrofitProvider);
  }

  public static SmartLockApi provideSmartLockApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideSmartLockApi(retrofit));
  }
}

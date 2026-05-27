package com.hnure.smartlock.ui.screens.profile;

import com.hnure.smartlock.data.local.TokenDataStore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class ProfileViewModel_Factory implements Factory<ProfileViewModel> {
  private final Provider<TokenDataStore> tokenDataStoreProvider;

  public ProfileViewModel_Factory(Provider<TokenDataStore> tokenDataStoreProvider) {
    this.tokenDataStoreProvider = tokenDataStoreProvider;
  }

  @Override
  public ProfileViewModel get() {
    return newInstance(tokenDataStoreProvider.get());
  }

  public static ProfileViewModel_Factory create(Provider<TokenDataStore> tokenDataStoreProvider) {
    return new ProfileViewModel_Factory(tokenDataStoreProvider);
  }

  public static ProfileViewModel newInstance(TokenDataStore tokenDataStore) {
    return new ProfileViewModel(tokenDataStore);
  }
}

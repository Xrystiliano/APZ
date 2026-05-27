package com.hnure.smartlock.ui.screens.lockdetail;

import com.hnure.smartlock.data.local.TokenDataStore;
import com.hnure.smartlock.data.repository.LockRepository;
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
public final class LockDetailViewModel_Factory implements Factory<LockDetailViewModel> {
  private final Provider<LockRepository> lockRepositoryProvider;

  private final Provider<TokenDataStore> tokenDataStoreProvider;

  public LockDetailViewModel_Factory(Provider<LockRepository> lockRepositoryProvider,
      Provider<TokenDataStore> tokenDataStoreProvider) {
    this.lockRepositoryProvider = lockRepositoryProvider;
    this.tokenDataStoreProvider = tokenDataStoreProvider;
  }

  @Override
  public LockDetailViewModel get() {
    return newInstance(lockRepositoryProvider.get(), tokenDataStoreProvider.get());
  }

  public static LockDetailViewModel_Factory create(Provider<LockRepository> lockRepositoryProvider,
      Provider<TokenDataStore> tokenDataStoreProvider) {
    return new LockDetailViewModel_Factory(lockRepositoryProvider, tokenDataStoreProvider);
  }

  public static LockDetailViewModel newInstance(LockRepository lockRepository,
      TokenDataStore tokenDataStore) {
    return new LockDetailViewModel(lockRepository, tokenDataStore);
  }
}

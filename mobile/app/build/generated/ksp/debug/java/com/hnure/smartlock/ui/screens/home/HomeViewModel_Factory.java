package com.hnure.smartlock.ui.screens.home;

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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<LockRepository> lockRepositoryProvider;

  public HomeViewModel_Factory(Provider<LockRepository> lockRepositoryProvider) {
    this.lockRepositoryProvider = lockRepositoryProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(lockRepositoryProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<LockRepository> lockRepositoryProvider) {
    return new HomeViewModel_Factory(lockRepositoryProvider);
  }

  public static HomeViewModel newInstance(LockRepository lockRepository) {
    return new HomeViewModel(lockRepository);
  }
}

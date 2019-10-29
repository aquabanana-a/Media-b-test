package com.dobranos.instories.view.injection.ui;

import android.app.Application;
import com.dobranos.instories.data.base.repository.MemoryRepositoryImpl;
import com.dobranos.instories.domain.base.repository.MemoryRepository;
import com.dobranos.instories.domain.base.resolver.ResolutionResolver;
import com.dobranos.instories.view.base.ui.App;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class AppModule
{
    private final App app;

    public AppModule(App app)
    {
        this.app = app;
    }

    @Provides
    @Singleton
    public Application provideApplication()
    {
        return app;
    }

    @Provides
    @Singleton
    public MemoryRepository provideMemoryRepository()
    {
        return new MemoryRepositoryImpl();
    }

    @Provides
    @Singleton
    public ResolutionResolver provideResolutionResolver()
    {
        return new ResolutionResolver();
    }
}

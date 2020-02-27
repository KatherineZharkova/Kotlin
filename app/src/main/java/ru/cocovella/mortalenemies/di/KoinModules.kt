package ru.cocovella.mortalenemies.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import ru.cocovella.mortalenemies.data.Repository
import ru.cocovella.mortalenemies.data.provider.FireStoreProvider
import ru.cocovella.mortalenemies.data.provider.RemoteDataProvider
import ru.cocovella.mortalenemies.view.list.ListViewModel
import ru.cocovella.mortalenemies.view.note.NoteViewModel
import ru.cocovella.mortalenemies.view.splash.SplashViewModel

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FireStoreProvider(get(), get()) } bind RemoteDataProvider::class
    single { Repository(get()) }
}

@ExperimentalCoroutinesApi
val listModule = module {
    viewModel { ListViewModel(get()) }
}

@ExperimentalCoroutinesApi
val noteModule = module {
    viewModel { NoteViewModel(get()) }
}

@ExperimentalCoroutinesApi
val splashModule = module {
    viewModel { SplashViewModel(get()) }
}
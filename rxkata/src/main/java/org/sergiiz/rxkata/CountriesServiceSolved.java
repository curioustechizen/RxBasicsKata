package org.sergiiz.rxkata;

import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Predicate;

class CountriesServiceSolved implements CountriesService {

    private static final int ONE_MILLION = 1_000_000;
    private final Predicate<Country> POPULATION_MORE_THAN_ONE_MILLION = country -> country.population > ONE_MILLION;

    @Override
    public Single<String> countryNameInCapitals(Country country) {
        return Single.just(country.name.toUpperCase());
    }

    public Single<Integer> countCountries(List<Country> countries) {
        return Single.just(countries.size());
    }

    public Observable<Long> listPopulationOfEachCountry(List<Country> countries) {
        return asObservable(countries).map(country -> country.population);
    }

    @Override
    public Observable<String> listNameOfEachCountry(List<Country> countries) {
        return asObservable(countries).map(country -> country.name);
    }

    @Override
    public Observable<Country> listOnly3rdAnd4thCountry(List<Country> countries) {
        return asObservable(countries)
                .skip(2)
                .take(2);
    }

    @Override
    public Single<Boolean> isAllCountriesPopulationMoreThanOneMillion(List<Country> countries) {
        return asObservable(countries).all(POPULATION_MORE_THAN_ONE_MILLION);
    }

    @Override
    public Observable<Country> listPopulationMoreThanOneMillion(List<Country> countries) {
        return asObservable(countries).filter(POPULATION_MORE_THAN_ONE_MILLION);
    }


    @Override
    public Observable<Country> listPopulationMoreThanOneMillion(FutureTask<List<Country>> countriesFromNetwork) {
        return Observable.fromFuture(countriesFromNetwork)
                .flatMap(Observable::fromIterable)
                .filter(POPULATION_MORE_THAN_ONE_MILLION);
    }

    @Override
    public Observable<String> getCurrencyUsdIfNotFound(String countryName, List<Country> countries) {
        return asObservable(countries)
                .filter(country -> country.name.endsWith(countryName))
                .map(country -> country.currency)
                .switchIfEmpty(Observable.just("USD"));
    }

    @Override
    public Observable<Long> sumPopulationOfCountries(List<Country> countries) {
        return listPopulationOfEachCountry(countries).reduce((aLong, aLong2) -> aLong + aLong2).toObservable();
    }

    @Override
    public Single<Map<String, Long>> mapCountriesToNamePopulation(List<Country> countries) {
        return asObservable(countries).toMap(country -> country.name, country -> country.population);
    }

    private Observable<Country> asObservable(List<Country> countries) {
        return Observable.fromIterable(countries);
    }
}

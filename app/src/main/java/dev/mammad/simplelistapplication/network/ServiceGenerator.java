package dev.mammad.simplelistapplication.network;

import java.util.concurrent.TimeUnit;

import dev.mammad.simplelistapplication.Consts;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ServiceGenerator {

    private static final String SERVICE_BASE_URL = Consts.BASE_URL;
    private static Retrofit retrofit;
    private static OkHttpClient.Builder httpClient;
    private static ServiceGenerator instance;
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(SERVICE_BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create());

    private ServiceGenerator() {
        init();
    }

    public static ServiceGenerator getInstance() {

        if (instance == null) {
            instance = new ServiceGenerator();
        }
        return instance;
    }

    public static <S> S createService(Class<S> serviceClass) {
        getInstance();
        return retrofit.create(serviceClass);
    }

    private static void init() {
        httpClient = new OkHttpClient.Builder();

        httpClient.connectTimeout(50, TimeUnit.SECONDS);
        httpClient.readTimeout(50, TimeUnit.SECONDS);
        httpClient.retryOnConnectionFailure(true);
        OkHttpClient client = httpClient.build();
        retrofit = builder.client(client).build();
    }
}

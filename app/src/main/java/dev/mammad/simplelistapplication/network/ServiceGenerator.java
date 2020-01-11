package dev.mammad.simplelistapplication.network;

import dev.mammad.simplelistapplication.config.Configurations;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * A generic service generator responsible for creating and initializing different
 * Retrofit clients.
 */
public class ServiceGenerator {

    /**
     * Given a type token of the target Retrofit service interface, it will create an instance
     * of that service.
     *
     * @param serviceClass The Retrofit service type token.
     * @param <S>          The service type.
     * @return Created service.
     */
    public static <S> S createService(Class<S> serviceClass) {
        return ServiceGeneratorHolder.INSTANCE.create(serviceClass);
    }

    /**
     * A lazy initialization holder class helping us to create the HTTP client and
     * related Retrofit stuff in a lazy fashion.
     *
     * @implNote See Item 83 of Effective Java
     */
    private static class ServiceGeneratorHolder {

        /**
         * Holds the lazy initialized Retrofit.
         */
        static Retrofit INSTANCE = initialize();

        private static Retrofit initialize() {
            OkHttpClient client = getHttpClient();

            return new Retrofit.Builder()
                    .baseUrl(Configurations.getBaseUrl())
                    .addConverterFactory(MoshiConverterFactory.create())
                    .client(client)
                    .build();
        }

        private static OkHttpClient getHttpClient() {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            httpClient.connectTimeout(Configurations.getConnectTimeout(), SECONDS);
            httpClient.readTimeout(Configurations.getReadTimeout(), SECONDS);
            httpClient.retryOnConnectionFailure(true);

            return httpClient.build();
        }
    }
}

package expo.modules.analytics.amplitude;

import android.content.Context;
import android.util.Log;

import com.amplitude.api.AmplitudeClient;
import com.amplitude.api.TrackingOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.unimodules.core.ExportedModule;
import org.unimodules.core.Promise;
import org.unimodules.core.interfaces.ExpoMethod;

public class AmplitudeModule extends ExportedModule {
  private AmplitudeClient mClient;
  private TrackingOptions mPendingTrackingOptions;

  public AmplitudeModule(Context context) {
    super(context);
  }

  @Override
  public String getName() {
    return "ExpoAmplitude";
  }

  @ExpoMethod
  public void initialize(final String apiKey, Promise promise) {
    resetAmplitudeDatabaseHelper();
    mClient = new AmplitudeClient();
    if (mPendingTrackingOptions != null) {
      mClient.setTrackingOptions(mPendingTrackingOptions);
    }
    mClient.initialize(getContext(), apiKey);
    promise.resolve(null);
  }

  @ExpoMethod
  public void setUserId(final String userId, Promise promise) {
    if (rejectUnlessInitialized(promise)) {
      return;
    }

    mClient.setUserId(userId);
    promise.resolve(null);
  }

  @ExpoMethod
  public void setUserProperties(final Map<String, Object> properties, Promise promise) {
    if (rejectUnlessInitialized(promise)) {
      return;
    }
    mClient.setUserProperties(new JSONObject(properties));
    promise.resolve(null);
  }

  @ExpoMethod
  public void clearUserProperties(Promise promise) {
    if (rejectUnlessInitialized(promise)) {
      return;
    }

    mClient.clearUserProperties();
    promise.resolve(null);
  }

  @ExpoMethod
  public void logEvent(final String eventName, Promise promise) {
    if (rejectUnlessInitialized(promise)) {
      return;
    }

    mClient.logEvent(eventName);
    promise.resolve(null);
  }

  @ExpoMethod
  public void logEventWithProperties(final String eventName, final Map<String, Object> properties, Promise promise) {
    if (rejectUnlessInitialized(promise)) {
      return;
    }

    mClient.logEvent(eventName, new JSONObject(properties));
    promise.resolve(null);
  }

  @ExpoMethod
  public void setGroup(final String groupType, final List<Object> groupNames, Promise promise) {
    if (rejectUnlessInitialized(promise)) {
      return;
    }

    mClient.setGroup(groupType, new JSONArray(groupNames));
    promise.resolve(null);
  }

  @ExpoMethod
  public void setTrackingOptions(final ReadableArguments options, Promise promise) {
    TrackingOptions trackingOptions = new TrackingOptions();

    if (options.getBoolean("disableAdid")) {
      trackingOptions.disableAdid();
    }
    if (options.getBoolean("disableCarrier")) {
      trackingOptions.disableCarrier();
    }
    if (options.getBoolean("disableCity")) {
      trackingOptions.disableCity();
    }
    if (options.getBoolean("disableCountry")) {
      trackingOptions.disableCountry();
    }
    if (options.getBoolean("disableDeviceBrand")) {
      trackingOptions.disableDeviceBrand();
    }
    if (options.getBoolean("disableDeviceModel")) {
      trackingOptions.disableDeviceModel();
    }
    if (options.getBoolean("disableDMA")) {
      trackingOptions.disableDma();
    }
    if (options.getBoolean("disableIPAddress")) {
      trackingOptions.disableIpAddress();
    }
    if (options.getBoolean("disableLanguage")) {
      trackingOptions.disableLanguage();
    }
    if (options.getBoolean("disableLatLng")) {
      trackingOptions.disableLatLng();
    }
    if (options.getBoolean("disableOSName")) {
      trackingOptions.disableOsName();
    }
    if (options.getBoolean("disableOSVersion")) {
      trackingOptions.disableOsVersion();
    }
    if (options.getBoolean("disablePlatform")) {
      trackingOptions.disablePlatform();
    }
    if (options.getBoolean("disableRegion")) {
      trackingOptions.disableRegion();
    }
    if (options.getBoolean("disableVersionName")) {
      trackingOptions.disableVersionName();
    }

    if (mClient != null) {
      mClient.setTrackingOptions(trackingOptions);
    } else {
      mPendingTrackingOptions = trackingOptions;
    }

    promise.resolve(null);
  }

  private boolean rejectUnlessInitialized(Promise promise) {
    if (mClient == null) {
      promise.reject("E_NO_INIT", "Amplitude client has not been initialized, are you sure you have configured it with #init(apiKey)?");
      return true;
    }
    return false;
  }

  private void resetAmplitudeDatabaseHelper() {
    try {
      Field field = Class.forName("com.amplitude.api.DatabaseHelper").getDeclaredField("instance");
      field.setAccessible(true);
      field.set(null, null);
    } catch (Throwable e) {
      Log.e(getName(), e.toString());
    }
  }
}

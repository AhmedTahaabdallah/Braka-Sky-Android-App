-ignorewarnings
#-dontwarn com.squareup.picasso.**
#-dontwarn com.squareup.okhttp.**
#-keep class android.support.v7.widget.SearchView { *; }
#-dontwarn okhttp3.**
#-dontwarn okio.**
#-dontwarn javax.annotation.**
#-dontwarn org.conscrypt.**
# A resource is loaded with a relative path so the package of this class must be preserved.
#-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-keep class androidx.appcompat.widget.** { *; }
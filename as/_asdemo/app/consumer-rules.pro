-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-keep class com.charles.agsdk.**.R

-keep public class com.charles.agsdk.** {
   public *;
}
#保持所有native方法
-keepclasseswithmembernames class * {
   native <methods>;
}


##保持libUvcCamera依赖库混淆

-keep public class com.charles.agsdk.camera.** {
   public  *;
   protected  *;
}
-keep class com.charles.agsdk.camera.UVCCamera {
  *;
}

#保持分屏服务的保护级别的方法，以便继承复写
-keep public class com.charles.agsdk.preso.* {
   protected *;
}


-keep public class org.greenrobot.eventbus.** {
   public *;
}
-keep public class com.commonsware.** {
   public *;
}

#eventbus混淆保持
-keep class org.greenrobot.eventbus.** {
   public *;
   protected *;
}
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }


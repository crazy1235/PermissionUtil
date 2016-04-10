### Simple Demo for PermissionUtil Library.


## How to use?

Add the following setting in your root build.gradle.

```
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}

```

Then add the dependency

```
    compile 'com.github.crazy1235:PermissionUtil:v1.0'

```

When you use activity to make a phone call, you must request the CALL_PHONE permission.

```

PermissionUtil.requestPermissions(MainActivity.this, CODE_REQUEST_CALL_PHONE, true, Manifest.permission.CALL_PHONE);

```

And you should add three functions, respectively authorized success, authorized failure, showRationale.
  
```
@OnPermissionGranted(CODE_REQUEST_CALL_PHONE)
void xxx(){
    //TODO request permission success and do what you want.
}

```

```
@OnPermissionDenied(CODE_REQUEST_CALL_PHONE)
void xxx(){
    //TODO request permission failure , you should not make a phone call and prompt to user. 
}

```

```
@ShowRationale(CODE_REQUEST_CALL_PHONE)
void xxx(){
    ///TODO show rationale about why you want to request the permission.
}

```

en ~ 
ok ~ 
its over~


Please star and fork me. thanks~
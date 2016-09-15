HOW TO USE GUID.
==============

1) Add dependancy in your app/build.gradle file like below
--------------

      android {
            ....
            packagingOptions {
                  exclude 'META-INF/DEPENDENCIES'
                  exclude 'META-INF/NOTICE'
                  exclude 'META-INF/LICENSE'
                  exclude 'META-INF/LICENSE.txt'
                  exclude 'META-INF/NOTICE.txt'
            }
      }

      dependencies {
            ....
            compile 'in.gauriinfotech.mylibraries:rest-client:1.0.0'
      }

2) Upload image using below code.
--------------

        File file = new File("/path/to/file");
        Map<String, String> params = new HashMap<>();
        params.put("user", "nirranjan"); // add here request parameters if any
        // display progress dialog if needed
        ImageUploader<OutputClass> uploader = new ImageUploader<>(SITE, params, OutputClass.class, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "ERROR :: " + Log.getStackTraceString(error));
            }
        }, new Response.Listener<OutputClass>() {
            @Override
            public void onResponse(OutputClass response) {
                Log.e(TAG, "SUCCESS ::  " + response.toString());
            }
        }, file); // file is Object of file which you want to upload to server.
        uploader.setTag("myTag");
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(uploader);

3) OutputClass is nothing but the output from server.
--------------

Consider server returning output as

    {
      "status":"success"
      "name":"fileName"
    }

then OutputClass should look like below.

    public class OutputClass {
      String status;

      public String getStatus() {
        return status;
      }

    }
    

If you have any doubts contact on nirranjan.raut@gmail.com

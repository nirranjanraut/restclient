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
            compile 'in.gauriinfotech:rest-client:1.0.1'
      }

2) Upload file using below code.
--------------

        File file = new File("/path/to/file");
        Map<String, String> params = new HashMap<>();
        params.put("user", "nirranjan"); // add here request parameters if any
        // display progress dialog if needed
        ImageUploader uploader = new ImageUploader("www.example.com/upload.php", params, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "ERROR :: " + Log.getStackTraceString(error));
                // dismiss dialog here if displaying
            }
        }, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "SUCCESS ::  " + response);
                // dismiss dialog here if displaying
            }
        }, file); // file is Object of file which you want to upload to server.
        uploader.setTag("myTag");
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(uploader); // file is uploaded on server using parameter name 'file'


If you have any doubts contact on nirranjan.raut@gmail.com

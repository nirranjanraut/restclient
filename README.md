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
            compile 'in.gauriinfotech:rest-client:1.0.0'
      }

2) Upload File using below code.
--------------

        File file = new File("/path/to/file");// make sure your app has READ_EXTERNAL_STORAGE permission granted.
        Map<String, String> params = new HashMap<>();
        params.put("user", "nirranjan"); // add here request parameters if any
        // display progress dialog if needed
        FileUploader uploader = new FileUploader("www.example.com/upload.php", params, new Response.ErrorListener() {
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

AND upload.php will look like below

    <?php
        ini_set('display_errors', 'on');
        ini_set('memory_limit', '512M');
        ini_set('upload_max_filesize', '10M');
        ini_set('post_max_size', '10M');
        $response = array();
        $STATUS = 'status';
        $SUCCESS = 'success';
        $uploadFileName  = $_FILES['file']['name'];
        $info = pathinfo($uploadFileName);
        $ext = $info['extension'];
        $response["size"] = $_FILES['file']['size'];
        if($_FILES['file']['size'] > 0 && isset($_POST['user'])) {
        	if(!is_dir('images')) {
        		mkdir('images');
        	}
        	$user = $_POST['user'];
        	$fileName;
        	$query;
        	$fileName = 'images/' . $user . '.' . $ext;
        	$response['name'] = $fileName;
        	$response['user'] = $user;
        	$tmpName  = $_FILES['file']['tmp_name'];
        	if(move_uploaded_file($tmpName, $fileName)) {
        		$response[$STATUS] = $SUCCESS;
        	} else {
        		$response[$STATUS] = "Failed to write file.";
        	}
        } else {
        	$response[$STATUS] = "Invalid input";
        }
        echo json_encode($response);
        ?>

If you have any doubts contact on nirranjan.raut@gmail.com

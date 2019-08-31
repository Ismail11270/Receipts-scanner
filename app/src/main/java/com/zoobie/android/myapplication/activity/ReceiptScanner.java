package com.zoobie.android.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.zoobie.android.myapplication.R;
import com.zoobie.android.myapplication.adapters.EditNewDataListAdapter;
import com.zoobie.android.myapplication.market.data.Product;
import com.zoobie.android.myapplication.market.data.Receipt;
import com.zoobie.android.myapplication.market.shops.Market;
import com.zoobie.android.myapplication.processing.ReceiptDataExtractor;
import com.zoobie.android.myapplication.storage.ProductsDB;

import java.util.ArrayList;


public class ReceiptScanner extends AppCompatActivity {

    private EditText mResultEt;
    private ImageView mImageIv;
    private Toolbar toolbar;

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;
    public static final int PICK_GALLERY = 1;
    public static final int PICK_CAMERA = 2;
    private int source;
    private RecyclerView recyclerView;
    private Bitmap bitmap;
    private EditNewDataListAdapter adapter;
    private String cameraPermission[];
    private String storagePermission[];
    private Intent intent;
    private Uri image_uri;
    private Market market;
    private LinearLayout parentView;
    private Receipt receipt;
    private ArrayList<Product> scannedProductsList;
    private Spinner selectStoreSpinner;

    private void initFields() {
        recyclerView = findViewById(R.id.products_edit_rv);
        intent = getIntent();
        mImageIv = findViewById(R.id.imageIv);
        toolbar = findViewById(R.id.toolBar);
        toolbar.setSubtitle("Edit & Save");
        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        parentView = findViewById(R.id.newProductsListLayout);
        setSupportActionBar(toolbar);
        setTitle("New Receipt");
        cameraPermission = new String[]{
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        storagePermission = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_recognizer);
        initFields();
        boolean isNew = intent.getBooleanExtra("new", true);
        if (isNew) {
            recognize();
            System.out.println("NEW RECEIPT");

        } else {
            int receiptId = intent.getIntExtra("receipt_id", -1);
            if (receiptId < 0) throw new Resources.NotFoundException("The receipt not found");
            else {
                System.out.println("EDITING RECEIPT");
                ProductsDB db = new ProductsDB(getApplicationContext());
                receipt = db.getSingleReceipt(receiptId);
                scannedProductsList = db.getProducts(receiptId);
                market = db.queryStore(receipt.getUniqueStoreId());
                initRecyclerView();
            }
        }
    }

    private void recognize() {
        source = intent.getIntExtra("source", PICK_CAMERA);
        if (source == PICK_CAMERA) {
            if (!checkCameraPersmission()) {
                //camera permission not allowed, request agian
                requestCameraPermission();
            } else {
                //permission allowed,take picture
                pickCamera();
            }
        } else if (source == PICK_GALLERY) {
            //gallery option clicked
            if (!CheckStoragePersmission()) {
                //camera permission not allowed, request agian
                requestStoragePermission();
            } else {
                //permission allowed,take picture
                pickGallery();
            }
        }
    }


    //actionbar menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        getMenuInflater().inflate(R.menu.new_receipt_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addImage) {
            showImageImportDialog();
        }
        if (id == R.id.cancel) {
            onBackPressed();
        }
        if (id == R.id.save_data) {
             adapter.saveData(market);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showImageImportDialog() {
        //items to display in dialog
        String items[] = {"Camera", "Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        // set title
        dialog.setTitle("New Image");
        dialog.setItems(items, (dialogInterface, i) -> {
            if (i == 0) {
                //camera option clicked
                if (!checkCameraPersmission()) {
                    //camera permission not allowed, request agian
                    requestCameraPermission();
                } else {
                    //permission allowed,take picture
                    pickCamera();
                }
            } else if (i == 1) {
                //gallery option clicked
                if (!CheckStoragePersmission()) {
                    //camera permission not allowed, request agian
                    requestStoragePermission();
                } else {
                    //permission allowed,take picture
                    pickGallery();
                }
            }
        });
        dialog.create().show();     //show dialog
    }

    //TODO
    private void recognizeText(Frame frame, TextRecognizer recognizer) {
        SparseArray<TextBlock> items = recognizer.detect(frame);
        //ToDO let user choose market, neptun by default now
        intiateStoreSelectDialog(items);
    }

    private void intiateStoreSelectDialog(SparseArray<TextBlock> items) {

        parentView.setVisibility(View.INVISIBLE);

        Dialog storeSelectionDialog = new Dialog(this);
        storeSelectionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        storeSelectionDialog.setCancelable(false);
        storeSelectionDialog.setContentView(R.layout.select_store_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(storeSelectionDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        TextView title = storeSelectionDialog.findViewById(R.id.dialogTitle);
        title.setText("Receipt Scan");

        Button selectBtn = storeSelectionDialog.findViewById(R.id.dialogSaveBtn);
        selectBtn.setText("Select");

        ImageButton cancelBtn = storeSelectionDialog.findViewById(R.id.dialogCancelBtn);
        cancelBtn.setOnClickListener(v -> {
            storeSelectionDialog.dismiss();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });
        selectStoreSpinner = storeSelectionDialog.findViewById(R.id.selectStoreSpinner);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.store_names, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectStoreSpinner.setAdapter(adapter1);

        selectStoreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                market = Market.getInstance(position,"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        selectBtn.setOnClickListener(view -> {
            ReceiptDataExtractor extractor = new ReceiptDataExtractor(items, market);
            scannedProductsList = new ArrayList<>();
            try {
                scannedProductsList = extractor.extractProducts();
                storeSelectionDialog.dismiss();
                initRecyclerView();
                parentView.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
                onBackPressed();
                Toast.makeText(getApplicationContext()
                        , "Failed to recognize, please try again", Toast.LENGTH_SHORT).show();
            }
        });
        storeSelectionDialog.show();
        storeSelectionDialog.getWindow().setAttributes(lp);
    }


    private void pickGallery() {
        //intent to choose pic from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickCamera() {
        //intent to take image from the camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NewPic");   //title of the picture
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to text"); //description
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean CheckStoragePersmission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPersmission() {
        boolean isCameraAllowed = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean isWriteStorageAllowed = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return isCameraAllowed && isWriteStorageAllowed;
    }

    //handle permission result


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted)
                        pickCamera();
                    else Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted)
                        pickGallery();
                    else Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //got image from gallery now crop it
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON) //enable image guidelines
                        .start(this);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //got image from camera now crop it
                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON) //enable image guidelines
                        .start(this);
            }
        } else if (resultCode == RESULT_CANCELED) onBackPressed();
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                mImageIv.setImageURI(resultUri);

                BitmapDrawable bitmapDrawable = (BitmapDrawable) mImageIv.getDrawable();
                bitmap = bitmapDrawable.getBitmap();

                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!recognizer.isOperational()) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    recognizeText(frame, recognizer);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //if there is any error show it
                Exception error = result.getError();
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void initRecyclerView() {
        if (receipt == null)
            adapter = new EditNewDataListAdapter(scannedProductsList, this);
        else
            adapter = new EditNewDataListAdapter(scannedProductsList, this, receipt);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finishActivity(0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity(0);
    }
}




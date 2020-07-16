package com.b1610701.tuvantuyensinh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.b1610701.tuvantuyensinh.fragments.CNTTFragment;
import com.b1610701.tuvantuyensinh.fragments.HomeFragment;
import com.b1610701.tuvantuyensinh.fragments.KDNNFragment;
import com.b1610701.tuvantuyensinh.fragments.KTNNFragment;
import com.b1610701.tuvantuyensinh.fragments.KTXDFragment;
import com.b1610701.tuvantuyensinh.fragments.LHCFragment;
import com.b1610701.tuvantuyensinh.fragments.NNAFragment;
import com.b1610701.tuvantuyensinh.fragments.QTKDFragment;
import com.b1610701.tuvantuyensinh.fragments.UserFragment;
import com.b1610701.tuvantuyensinh.fragments.VNHFragment;
import com.b1610701.tuvantuyensinh.model.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private View headerView;
    RelativeLayout profile_layout;
    CircleImageView profile_image;
    TextView profile_fullname;
    TextView profile_username;
    LinearLayout q_a_layout;
    ImageView btn_change_image;
    private String UID;
    boolean isAdmin = false;

    boolean doubleBackToExitPressedOnce = false;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    private String guestname = "";
    public static String guestusername = "guest";
    public static String guestemail = "guest_0000@tuvantuyensinh.ctu.edu.vn";
    public static String guestpassword = "password";
    public static String NGANH = "";

    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
    @Override
    protected void onStart() {
        super.onStart();

        headerView = LayoutInflater.from(this).inflate(R.layout.nav_header, navigationView, false);
        navigationView.addHeaderView(headerView);
        profile_layout = headerView.findViewById(R.id.profile_layout);
        profile_image = headerView.findViewById(R.id.profile_image);
        profile_fullname = headerView.findViewById(R.id.profile_fullname);
        profile_username = headerView.findViewById(R.id.profile_username);
        btn_change_image = headerView.findViewById(R.id.iv_change_image);

        btn_change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            UID = firebaseUser.getUid();
            navigationView.getMenu().findItem(R.id.item5).setVisible(false);
            navigationView.getMenu().findItem(R.id.item6).setVisible(true);
            profile_layout.setVisibility(View.VISIBLE);
            reference = FirebaseDatabase.getInstance().getReference("Users").child(UID);
            reference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user != null){
                        if (user.getUsername().contains("guest_")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                profile_fullname.setAutoSizeTextTypeUniformWithConfiguration(16, 24, 2, TypedValue.COMPLEX_UNIT_DIP);
                            }
                            guestname = user.getFullname();
                            guestusername = user.getUsername();
                            guestemail = user.getEmail();

                            profile_image.setImageResource(R.drawable.user);
                            profile_fullname.setText(guestname);
                            profile_username.setText(guestusername);
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                profile_fullname.setAutoSizeTextTypeUniformWithConfiguration(16, 24, 2, TypedValue.COMPLEX_UNIT_DIP);
                            }
                            profile_username.setText(user.getUsername()+"");
                            profile_fullname.setText(user.getFullname()+"");

                            if (user.getImageURL().equals("default")){
                                profile_image.setImageResource(R.drawable.user);
                            } else {
                                if (!MainActivity.this.isDestroyed()){
                                    Glide.with(MainActivity.this)
                                            .load(user.getImageURL())
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true)
                                            .into(profile_image);
                                }
                            }
                            if (user.getEmail().contains("@ctu.edu.vn") || user.getEmail().contains("@cit.ctu.edu.vn")) isAdmin = true;
                        }
                    } else {
                        Log.d("TAG", "user is null");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            navigationView.getMenu().findItem(R.id.item6).setVisible(false);
            navigationView.getMenu().findItem(R.id.item5).setVisible(true);
            profile_layout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView btnMenu = findViewById(R.id.btnMenu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayShowTitleEnabled(false);

        storageReference = FirebaseStorage.getInstance().getReference("Uploads");

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        q_a_layout = findViewById(R.id.Q_A);

        q_a_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseUser != null){
                    if (isAdmin){
                        Fragment userFragment = new UserFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, userFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        mDrawerLayout.closeDrawers();
                    } else {
                        Intent intent = new Intent(MainActivity.this, QAActivity.class);
                        intent.putExtra("UID", UID);
                        intent.putExtra("FULLNAME", guestname);
                        startActivity(intent);
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.InputDialog));
                    builder.setTitle("Q&A System");
                    builder.setMessage(getResources().getString(R.string.whatshouldwecallyou));
                    // Set up the input
                    final EditText input = new EditText(getApplicationContext());
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    // Set up the buttons
                    builder.setNeutralButton("LOGIN", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.putExtra("QA", true);
                            intent.putExtra("UID", UID);
                            startActivity(intent);
                        }
                    });
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int randomInt = ThreadLocalRandom.current().nextInt(1000, 9999 + 1);
                            guestusername = "guest_" + randomInt;
                            guestemail = guestusername + "@tuvantuyensinh.ctu.edu.vn";
                            Intent intent = new Intent(MainActivity.this, QAActivity.class);
                            guestname = input.getText().toString();
                            if (guestname.equals("")) {
                                Toast.makeText(MainActivity.this, getResources().getString(R.string.notnull), Toast.LENGTH_SHORT).show();
                            } else {
                                intent.putExtra("FULLNAME", guestname);
                                intent.putExtra("UID", UID);
                                finish();
                                startActivity(getIntent());
                                startActivity(intent);
                            }
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }
        });

        SpannableString dangxuat = new SpannableString(getResources().getString(R.string.logout));
        dangxuat.setSpan(new ForegroundColorSpan(Color.RED), 0, dangxuat.length(), 0);
        SpannableString dangnhap = new SpannableString(getResources().getString(R.string.login));
        dangnhap.setSpan(new ForegroundColorSpan(Color.BLUE), 0, dangnhap.length(), 0);
        navigationView.getMenu().findItem(R.id.item6).setTitle(dangxuat);
        navigationView.getMenu().findItem(R.id.item5).setTitle(dangnhap);

        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                    }

                    @Override
                    public void onDrawerOpened(@NonNull View drawerView) {
                    }

                    @Override
                    public void onDrawerClosed(@NonNull View drawerView) {
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                    }
                }
        );

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        // set item as selected to persist highlight
//                        menuItem.setChecked(true);
                        switch (menuItem.getItemId()){
                            case R.id.item1:
                                GotoFragment(new HomeFragment());
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.item2:
                                popupMenu();
                                break;
                            case R.id.item5:
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.item6:
                                mDrawerLayout.closeDrawers();
                                Log.d("TAG", "user: "+guestusername);
                                if (guestusername.contains("guest_")) {
                                    DeleteUser(guestemail, guestpassword);
                                    navigationView.getMenu().findItem(R.id.item5).setVisible(true);
                                    navigationView.getMenu().findItem(R.id.item6).setVisible(false);
                                    profile_layout.setVisibility(View.INVISIBLE);
                                    Toast.makeText(MainActivity.this, getResources().getString(R.string.loggingout), Toast.LENGTH_SHORT).show();
                                } else {
                                    FirebaseAuth.getInstance().signOut();
                                    navigationView.getMenu().findItem(R.id.item5).setVisible(true);
                                    navigationView.getMenu().findItem(R.id.item6).setVisible(false);
                                    profile_layout.setVisibility(View.INVISIBLE);
                                    Toast.makeText(MainActivity.this, getResources().getString(R.string.success_logout), Toast.LENGTH_LONG).show();
                                    finish();
                                    startActivity(getIntent());
                                }
                            default:
                        }
                        return true;
                    }
                });

        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            HomeFragment firstFragment = new HomeFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
        }
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(MainActivity.this);
        pd.setMessage(getResources().getString(R.string.uploading));
        pd.show();
        if (imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() +"."+ getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("imageURL", mUri);
                        reference.updateChildren(hashMap);
                    } else {
                        Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                    pd.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.noimageselected), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(MainActivity.this, getResources().getString(R.string.uploadInProgress), Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }

    private void DeleteUser(String email, String password){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        assert user != null;
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(MainActivity.this, getResources().getString(R.string.success_logout), Toast.LENGTH_LONG).show();
                                            Log.d("TAG", "User account deleted.");
                                            DeleteUserData(guestusername);
                                            FirebaseAuth.getInstance().signOut();
                                            finish();
                                            startActivity(getIntent());
                                        }
                                    }
                                });

                    }
                });
    }

    private void DeleteUserData(String username){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query guests = ref.child("Users").orderByChild("username").equalTo(username);

        guests.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot guestSnapshot: dataSnapshot.getChildren()) {
                    guestSnapshot.getRef().removeValue();
                }
                MainActivity.guestusername = "guest";
                MainActivity.guestemail = "";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void GotoFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    protected void onResume() {
        if (navigationView.getHeaderView(1) != null){
            navigationView.removeHeaderView(headerView);
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.exit_confirm), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void popupMenu(){
        PopupMenu popup = new PopupMenu(MainActivity.this, navigationView.findViewById(R.id.item2));
        popup.getMenuInflater().inflate(R.menu.nganh, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item2) {
                mDrawerLayout.closeDrawers();
                switch (item2.getItemId()) {
                    case R.id.cntt:
                        GotoFragment(new CNTTFragment());
                        NGANH = "cntt";
                        break;
                    case R.id.kinhdoanhnongnghiep:
                        GotoFragment(new KDNNFragment());
                        NGANH = "kdnn";
                        break;
                    case R.id.kinhtenongnghiep:
                        GotoFragment(new KTNNFragment());
                        NGANH = "ktnn";
                        break;
                    case R.id.kythuatxaydung:
                        GotoFragment(new KTXDFragment());
                        NGANH = "ktxd";
                        break;
                    case R.id.luathanhchinh:
                        GotoFragment(new LHCFragment());
                        NGANH = "lhc";
                        break;
                    case R.id.ngonnguanh:
                        GotoFragment(new NNAFragment());
                        NGANH = "nna";
                        break;
                    case R.id.quantrikinhdoanh:
                        GotoFragment(new QTKDFragment());
                        NGANH = "qtkd";
                        break;
                    case R.id.vietnamhoc:
                        GotoFragment(new VNHFragment());
                        NGANH = "vnh";
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    //    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.home:
//                mDrawerLayout.openDrawer(GravityCompat.START);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
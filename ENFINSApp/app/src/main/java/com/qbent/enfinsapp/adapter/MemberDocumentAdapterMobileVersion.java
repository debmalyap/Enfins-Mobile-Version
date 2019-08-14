package com.qbent.enfinsapp.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.qbent.enfinsapp.MemberEditActivity;
import com.qbent.enfinsapp.R;
import com.qbent.enfinsapp.global.AlertDialogue;
import com.qbent.enfinsapp.global.GlobalImageSettings;
import com.qbent.enfinsapp.model.ApiRequest;
import com.qbent.enfinsapp.model.ImageLoader;
import com.qbent.enfinsapp.model.MemberKycDocument;
import com.qbent.enfinsapp.restapi.ApiHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class MemberDocumentAdapterMobileVersion extends RecyclerView.Adapter<MemberDocumentAdapterMobileVersion.MemberDocumentViewHolder>{

    private final List<MemberKycDocument> memberKycDocuments;
    private List<MemberKycDocument> _retData;
    private Context context;
    HashMap<String, String> spinnerIdTypeMap = new HashMap<String, String>();
    Integer REQUEST_CAMERA = 1, SELECT_FILE = 2;
    private Bitmap bitmap;
    String json,image,date1,date2;
    private Date fromDate,toDate;
    private DatePickerDialog.OnDateSetListener fromDocumentDateSetListener,toDocumentDateSetListener;
    String emptyGuid = "00000000-0000-0000-0000-000000000000";


    public MemberDocumentAdapterMobileVersion(List<MemberKycDocument> memberKycDocuments,Context context) {
        this.memberKycDocuments = memberKycDocuments;
        this._retData = memberKycDocuments;
        this.context = context;
    }

    @NonNull
    @Override
    public MemberDocumentAdapterMobileVersion.MemberDocumentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_member_document_detail_normal, viewGroup, false);
        return new MemberDocumentAdapterMobileVersion.MemberDocumentViewHolder(view);
    }


    private void SelectImage(final int position)
    {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(items[i].equals("Camera"))
                {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    ((MemberEditActivity) context).startActivityForResult(cameraIntent,Integer.valueOf(String.valueOf(REQUEST_CAMERA) + String.valueOf(position)));
                }
                else if(items[i].equals("Gallery"))
                {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryIntent.setType("image/*");
                    ((MemberEditActivity) context).startActivityForResult(galleryIntent,Integer.valueOf(String.valueOf(SELECT_FILE) + String.valueOf(position)));
                }
                else if(items[i].equals("Cancel"))
                {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }





    @Override
    public void onBindViewHolder(@NonNull final MemberDocumentAdapterMobileVersion.MemberDocumentViewHolder memberDocumentViewHolder, final int i) {

        final AlertDialogue alertDialogue = new AlertDialogue(context);



        memberDocumentViewHolder.mItem = memberKycDocuments.get(i);
        memberDocumentViewHolder.mNumberView.setText(memberKycDocuments.get(i).getKycdocumentNo());
        if(memberKycDocuments.get(i).getIssueDate()!=null && !memberKycDocuments.get(i).getIssueDate().equals("null"))
        {
            memberDocumentViewHolder.mIssueDateView.setText(memberKycDocuments.get(i).getIssueDate());
        }
        else
        {
            memberDocumentViewHolder.mIssueDateView.setText("");
        }

        if(memberKycDocuments.get(i).getExpiryDate()!=null && !memberKycDocuments.get(i).getExpiryDate().equals("null"))
        {
            memberDocumentViewHolder.mExpiryDateView.setText(memberKycDocuments.get(i).getExpiryDate());
        }
        else
        {
            memberDocumentViewHolder.mExpiryDateView.setText("");
        }

        memberDocumentViewHolder.mImageView.setImageBitmap(null);

        memberDocumentViewHolder.mImageUploadedView.setVisibility(View.GONE);

        memberDocumentViewHolder.mChooseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SelectImage(i);
            }
        });

        if(memberKycDocuments.size() == 1)
        {
            memberDocumentViewHolder.mImageDeletView.setVisibility(View.GONE);
        }
        else {
            memberDocumentViewHolder.mImageDeletView.setVisibility(View.VISIBLE);
        }

        memberDocumentViewHolder.mImageDeletView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("ENFIN Admin");
                builder.setMessage("Are you sure to delete the document?");
                builder.setCancelable(true);
                builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int k)
                    {
                        removeItem(i);

                    }
                });
                builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int k)
                    {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });

        memberDocumentViewHolder.mIssueDateView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(false);
                return false;
            }
        });

        memberDocumentViewHolder.mIssueDateView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        view.getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        fromDocumentDateSetListener,
                        year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        fromDocumentDateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @SuppressLint("LongLogTag")
            @Override
            public void onDateSet(DatePicker datePicker, int day, int month, int year)
            {

                month += 1;
                date1 = month + "-" +year+ "-" +day;
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM-dd-yyyy");
                try {
                    fromDate = simpleDateFormat1.parse(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                memberDocumentViewHolder.mIssueDateView.setText(simpleDateFormat1.format(fromDate));
                _retData.get(i).setIssueDate(memberDocumentViewHolder.mIssueDateView.getText().toString());
            }
        };

        memberDocumentViewHolder.mExpiryDateView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(false);
                return false;
            }
        });

        memberDocumentViewHolder.mExpiryDateView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        view.getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        toDocumentDateSetListener,
                        year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        toDocumentDateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @SuppressLint("LongLogTag")
            @Override
            public void onDateSet(DatePicker datePicker, int day, int month, int year)
            {

                month += 1;
                date2 = month + "-" +year+ "-" +day;
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM-dd-yyyy");
                try {
                    toDate = simpleDateFormat1.parse(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                memberDocumentViewHolder.mExpiryDateView.setText(simpleDateFormat1.format(toDate));
                _retData.get(i).setExpiryDate(memberDocumentViewHolder.mExpiryDateView.getText().toString());
            }
        };

        memberDocumentViewHolder.mNumberView.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if((_retData.size()-1)>=i)
                {
                    _retData.get(i).setKycdocumentNo(s.toString());
                }

            }
        });

        final int pos = i;

        if(memberKycDocuments.get(i).getKycdocumentName()!=null && !memberKycDocuments.get(i).getKycdocumentName().equals("null") && !memberKycDocuments.get(i).getKycdocumentName().isEmpty())
        {


            memberDocumentViewHolder.mImageUploadedView.setVisibility(View.VISIBLE);

            String passbookFileName = memberKycDocuments.get(i).getKycdocumentName();
            int loader = R.drawable.dummy_upload_img;
            String image_url = GlobalImageSettings.API_BASE_URL.getKey() + passbookFileName;

//            if(getPath(Uri.parse(image_url)) == null)
//            {
//                return;
//            }

//            URL myUrl = null;
//            try {
//                myUrl = new URL(image_url);
//                HttpURLConnection restConnection = (HttpURLConnection) myUrl.openConnection();
//                restConnection.setConnectTimeout(1000 * 60 * 60);
//                restConnection.connect();
//                if (restConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//                    return;
//                }
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


            ImageLoader imgLoader = new ImageLoader(context);

            imgLoader.DisplayImage(image_url, loader, memberDocumentViewHolder.mImageView);

        }

        if(memberKycDocuments.get(i).getBitmap()!=null)
        {
            memberDocumentViewHolder.mImageView.setImageBitmap(memberKycDocuments.get(i).getBitmap());
            json = memberKycDocuments.get(i).getJson();
            image = memberKycDocuments.get(i).getImage();
            _retData.get(i).setKycdocumentScan(image);
        }

        memberDocumentViewHolder.mImageUploadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                if(memberKycDocuments.get(i).getBitmap()==null)
                {
                    alertDialogue.showAlertMessage("Kindly select image");
                    return;
                }
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("ENFIN Admin");
                builder.setMessage("Are you sure to upload this image?");
                builder.setCancelable(true);
                builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        ApiRequest apiRequest = new ApiRequest("uploadPassbookFile2");
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.accumulate("image",json);
                            jsonObject.accumulate("fileName",image);
                            jsonObject.accumulate("position",pos);
                            apiRequest.set_t(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new ApiHandler.PostAsync(context).execute(apiRequest);
                    }
                });
                builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });



//        String passbookFileName = memberKycDocuments.get(i).getKycdocumentName();
//        int loader = R.drawable.dummy_upload_img;
//        String image_url = GlobalImageSettings.API_BASE_URL.getKey() + passbookFileName;
//        ImageLoader imgLoader = new ImageLoader(memberDocumentViewHolder.itemView.getContext());
//
//        imgLoader.DisplayImage(image_url, loader, memberDocumentViewHolder.mImageView);

        String[] spinnerDocumentsArray = new String[memberKycDocuments.get(i).getDocuments().size()];
        for (int k = 0; k < memberKycDocuments.get(i).getDocuments().size(); k++)
        {
            spinnerIdTypeMap.put(memberKycDocuments.get(i).getDocuments().get(k).getId(),memberKycDocuments.get(i).getDocuments().get(k).getName());
            spinnerDocumentsArray[k] = memberKycDocuments.get(i).getDocuments().get(k).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,spinnerDocumentsArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        memberDocumentViewHolder.mTypeSpinnerView.setAdapter(adapter);

        if(memberKycDocuments.get(i).getKycdocumentId()!=null &&!memberKycDocuments.get(i).getKycdocumentId().isEmpty() && !memberKycDocuments.get(i).getKycdocumentId().equals(emptyGuid))
        {
            List<String> indexes1 = new ArrayList<String>(spinnerIdTypeMap.keySet());
            int test = indexes1.indexOf(memberKycDocuments.get(i).getKycdocumentId());
            String test2 = (new ArrayList<String>(spinnerIdTypeMap.values())).get(test);
            memberDocumentViewHolder.mTypeSpinnerView.setSelection(((ArrayAdapter<String>)memberDocumentViewHolder.mTypeSpinnerView.getAdapter()).getPosition(test2));

        }



//
        memberDocumentViewHolder.mTypeSpinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String test = "a";
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int n, long l) {

                String name = memberDocumentViewHolder.mTypeSpinnerView.getSelectedItem().toString();
                List<String> indexes = new ArrayList<String>(spinnerIdTypeMap.values());
                int a = indexes.indexOf(name);
                String stateId = (new ArrayList<String>(spinnerIdTypeMap.keySet())).get(indexes.indexOf(name));
                memberKycDocuments.get(i).setKycdocumentId(stateId);
                _retData.get(i).setKycdocumentId(stateId);



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }

        });





//        MemberDocumentViewHolder.gridLayout.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                //Toast.makeText(view.getContext(),"You Clicked "+members.get(i).getFullName(), Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(view.getContext(), CollectionPointDetailActivity.class);
//                intent.putExtra("collection_id",collectionPoints.get(i).getId());
//                view.getContext().startActivity(intent);
//
//            }
//        });
    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public void removeItem(int position)
    {
//        ((MemberEditActivity)context).removeDocument(position);
        memberKycDocuments.remove(position);
        _retData = memberKycDocuments;
        notifyItemRemoved(position);
        notifyDataSetChanged();

    }

    public List<MemberKycDocument> retrieveKycData()
    {

        return _retData;
    }

    @Override
    public int getItemCount() {
        return memberKycDocuments.size();
    }



    public class MemberDocumentViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final EditText mNumberView;
        public final EditText mIssueDateView;
        public final EditText mExpiryDateView;
        public final ImageView mImageView;
        public final ImageButton mImageUploadedView;
        public final ImageButton mChooseImageView;
        public final Spinner mTypeSpinnerView;
        public final Button mImageUploadView;
        public final Button mImageDeletView;
        public MemberKycDocument mItem;

        public MemberDocumentViewHolder(View view) {
            super(view);
            mView = view;
            mNumberView = (EditText) view.findViewById(R.id.member_document_number);
            mIssueDateView = (EditText) view.findViewById(R.id.member_document_issue_date);
            mExpiryDateView = (EditText) view.findViewById(R.id.member_document_expiry_date);
            mImageView = (ImageView) view.findViewById(R.id.member_document_image);
            mImageUploadedView = (ImageButton) view.findViewById(R.id.member_document_image_uploaded);
            mChooseImageView = (ImageButton) view.findViewById(R.id.member_document_choose_image);
            mTypeSpinnerView = (Spinner) view.findViewById(R.id.member_document_type_spinner);
            mImageUploadView = (Button) view.findViewById(R.id.button_document_upload);
            mImageDeletView = (Button) view.findViewById(R.id.button_document_delete);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNumberView.getText() + "'";
        }
    }
}

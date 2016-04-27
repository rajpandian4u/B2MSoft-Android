package com.pos1;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private ArrayList<String[]> contactList;
    private Activity activity;

    public ContactAdapter(ArrayList<String[]> contactList, Activity activity) {
            this.contactList = contactList;
            this.activity = activity;
    }

    @Override
    public int getItemCount() {
          return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
    	
        SharedPreferences prefs = activity.getSharedPreferences("medspref", Context.MODE_PRIVATE);        
       
        contactViewHolder.patientID.setText(prefs.getString("patientId", ""));
        contactViewHolder.fName.setText(prefs.getString("fname", ""));
        
        contactViewHolder.comment.setText(prefs.getString("comment", ""));
        contactViewHolder.medRecords.setText(prefs.getString("records", ""));
        contactViewHolder.income.setText(prefs.getString("income", ""));
        contactViewHolder.ssn.setText(prefs.getString("ssn", ""));
        contactViewHolder.contactPh.setText(prefs.getString("cphone", ""));
        contactViewHolder.contactName.setText(prefs.getString("cname", ""));
        contactViewHolder.mob.setText(prefs.getString("mobile", ""));
        contactViewHolder.homePh.setText(prefs.getString("hphone", ""));
        contactViewHolder.zip.setText(prefs.getString("zip", ""));
        contactViewHolder.state.setText(prefs.getString("state", ""));
        contactViewHolder.city.setText(prefs.getString("city", ""));
        contactViewHolder.add2.setText(prefs.getString("address2", ""));
        contactViewHolder.add1.setText(prefs.getString("address1", ""));
        contactViewHolder.disabled.setText(prefs.getString("disabled", ""));
        contactViewHolder.email.setText(prefs.getString("email", ""));
        contactViewHolder.child.setText(prefs.getString("children", ""));
        contactViewHolder.marriedStatus.setText(prefs.getString("married", ""));
        contactViewHolder.dob.setText(prefs.getString("dob", ""));
        contactViewHolder.lName.setText(prefs.getString("lname", "")); 
        contactViewHolder.mName.setText(prefs.getString("mname", ""));
        String attachedData1 = prefs.getString("documenttype1", "");
        String attachedData2 = prefs.getString("documenttype2", "");
        String attachedData3 = prefs.getString("documenttype3", "");
        String attachedData4 = prefs.getString("documenttype4", "");
        /*
        RG- Insurance field mapping on summary page
        tvcompname, tvmemberid,tvplannum,tvgroupnum,tvinsurancecomments

         */
        contactViewHolder.tvcompname.setText(prefs.getString("compname", ""));
        contactViewHolder.tvmemberid.setText(prefs.getString("plannum", ""));
        contactViewHolder.tvplannum.setText(prefs.getString("memberid", ""));
        contactViewHolder.tvgroupnum.setText(prefs.getString("groupnum", ""));
        contactViewHolder.tvinsurancecomments.setText(prefs.getString("insurancecomments", ""));

                
        if(!attachedData1.equals("")) {
            contactViewHolder.attachmentImg.setVisibility(View.VISIBLE);

            contactViewHolder.attach1.setVisibility(View.VISIBLE);
            contactViewHolder.attach1.setText("1. " + attachedData1);
        }
        if(!attachedData2.equals("")) {
        	contactViewHolder.attach2.setVisibility(View.VISIBLE);
           	contactViewHolder.attach2.setText("2. "+attachedData2);
        }
        	
        if(!attachedData3.equals("")) {
        	contactViewHolder.attach3.setVisibility(View.VISIBLE);
           	contactViewHolder.attach3.setText("3. "+attachedData3);
        }
        	
        if(!attachedData4.equals("")) {
        	contactViewHolder.attach4.setVisibility(View.VISIBLE);
           	contactViewHolder.attach4.setText("4. "+attachedData4);
        }
        	
        /*} else {
        	contactViewHolder.attach4.setVisibility(View.GONE);
        	contactViewHolder.attach3.setVisibility(View.GONE);
        	contactViewHolder.attach2.setVisibility(View.GONE);
        	contactViewHolder.attach1.setVisibility(View.GONE);
        	contactViewHolder.attachmentImg.setVisibility(View.GONE);
        }
        */
   }

   @Override
   public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.news_item_card_view, viewGroup, false);
        return new ContactViewHolder(itemView);
   }

  public static class ContactViewHolder extends RecyclerView.ViewHolder {
      CardView cv;
      TextView patientID, attach1, attach2, attach3, attach4;
      TextView fName, lName, dob, marriedStatus, child, email, disabled, add1, add2, city, state;
      TextView mName, zip, homePh, mob, contactName, contactPh, ssn, income, medRecords, comment;
      TextView tvcompname, tvmemberid,tvplannum,tvgroupnum,tvinsurancecomments;
      ImageView attachmentImg;

      ContactViewHolder(View itemView) {
          super(itemView);
          
           
          
          cv = (CardView)itemView.findViewById(R.id.cv);          
          
          patientID = (TextView)itemView.findViewById(R.id.patient_id);         
          
          fName = (TextView)itemView.findViewById(R.id.name);
          
          dob = (TextView)itemView.findViewById(R.id.dobirth);
          
          mName = (TextView)itemView.findViewById(R.id.mid_name);
          
          zip = (TextView)itemView.findViewById(R.id.zip);
          
          lName = (TextView)itemView.findViewById(R.id.last_name);
         
          
          marriedStatus = (TextView)itemView.findViewById(R.id.married);
          
          
          child = (TextView)itemView.findViewById(R.id.children);
          
          
          email = (TextView)itemView.findViewById(R.id.email);
         
          contactPh = (TextView)itemView.findViewById(R.id.cphone);
          
          disabled = (TextView)itemView.findViewById(R.id.disabled);
          
          
          add1 = (TextView)itemView.findViewById(R.id.add1);
          
          
          add2 = (TextView)itemView.findViewById(R.id.add2);
          
          
          city = (TextView)itemView.findViewById(R.id.city_name);
          
          
          state = (TextView)itemView.findViewById(R.id.state_name);
          
          
          homePh = (TextView)itemView.findViewById(R.id.hphone);
         
          
          mob = (TextView)itemView.findViewById(R.id.mobile);
          
          
          contactName = (TextView)itemView.findViewById(R.id.cname);
          
          
          ssn = (TextView)itemView.findViewById(R.id.ssn_name);
          
          
          income = (TextView)itemView.findViewById(R.id.income);
          
          
          medRecords = (TextView)itemView.findViewById(R.id.records);  
          
          
          comment = (TextView)itemView.findViewById(R.id.comment);
          
          /*
          RG- Insurance details fields
          <!-- scompname, smemberid,splannum,sgroupnum,sinsurcomment-->
           */

          tvcompname = (TextView)itemView.findViewById(R.id.scompname);
          tvmemberid = (TextView)itemView.findViewById(R.id.smemberid);
          tvplannum = (TextView)itemView.findViewById(R.id.splannum);
          tvgroupnum = (TextView)itemView.findViewById(R.id.sgroupnum);
          tvinsurancecomments = (TextView)itemView.findViewById(R.id.sinsurcomment);

          attachmentImg = (ImageView) itemView.findViewById(R.id.attachment_img);
          
          attach1 = (TextView)itemView.findViewById(R.id.attachment1);
          
          
          attach2 = (TextView)itemView.findViewById(R.id.attachment2);
          
          
          attach3 = (TextView)itemView.findViewById(R.id.attachment3); 
          
          
          attach4 = (TextView)itemView.findViewById(R.id.attachment4);
         
          
      }
    }
}
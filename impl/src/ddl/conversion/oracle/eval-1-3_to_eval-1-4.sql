-- Oracle conversion script - 1.3 to 1.4 
alter table EVAL_EVALUATION add (LOCAL_SELECTOR varchar2(255 char));

-- AVAILABLE_EMAIL_SENT was included in ddl files but not in hibernate mapping files 
-- alter table EVAL_EVALUATION add (AVAILABLE_EMAIL_SENT number(1,0));
-- If AVAILABLE_EMAIL_SENT is being added, give it a reasonable default -- assume emails have been sent for Active evals and not for others?
-- update EVAL_EVALUATION set AVAILABLE_EMAIL_SENT='1' where STATE='Active' or STATE='Closed';
-- update EVAL_EVALUATION set AVAILABLE_EMAIL_SENT='0' where AVAILABLE_EMAIL_SENT is null;

update eval_email_template set template_type='ConsolidatedAvailable' where template_type='SingleEmailAvailable';
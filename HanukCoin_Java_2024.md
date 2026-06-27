# פרוייקט HanukCoin

אודיסיאה \- מדמ״ח , אוקטובר 2024

## שלבים בפרוייקט

6/1 צריך לסיים, לעבור להתכונן למבחן  
30.12 מסיבת חנוכה במקום שעור  
13.1 שעור אחרון. שעור מיוחד במרץ

|  | שלב | תאריך סיום/הגשה |
| ----: | ----: | ----: |
| 1 | חלוקה לצוותים והרשאות | אתמול |
| 2 | תוכנית להצגת מצב הרשת | אתמול |
| 3 | פרסום מבנה הרשת \- ניסוי | 11/12/2024 |
| 4 | פרסום מבנה הרשת \+ שמירה/התאוששות מקובץ \- נסוי | 18/12/2024 |
| 5 | פרסום מבנה הרשת תחרות up-time למשך 4 ימים והגשת קוד | 1/1/2025 |
| 6 | כריית מטבעות \- ניסוי | 17/1/2025 |
| 7 | כריית מטבעות שלב התחרות למשך שבוע | 23/1/2025 |
| 8 | הגשת קוד סופי | 26/1/2025 |

## 

## שלב \- חלוקה לצוותים והרשאות

אורך השלב \- שבוע  
תפוקות \- שמות האנשים בצוות, חיבור המרצה לצוות ב-github/bitbucket, שרת פשוט שרץ אצל אחד המשתתפים (או יותר) ומאפשר חיבור אל הבית מבחוץ

Shrek \- אוהד ע. דניאל ב.  
Spooderman \- איל א. מיכאל ר.   \-  Connect two servers vie ngrok  
LordFarquad \- עומר כ. בארי ל. \- mine. connect on same LAN  
Donkey \- שחר פ. דניאל פ. \- writing in Rust.   
Gru \- עומרי מ. ניר א. \- Connect in same LAN. start on port-forwarding. Mine.  
CaptainAmerica \- איתמר צ. דניאל פ. \- connect via ngrok+ validate  
GlobgloGabgalab \-  יואב ע. עידו פ. \- Same LAN connect. Mine.   
DogPool \- עילי ח. עידן ל. mine. No connection  
Vector \- רתם ב. אורי ג. \- Mine. No connection  
Gollum \- איתי ק. אביה א. \- Same LAN connection  
PigiBank \- יהלי כ. סופי א. \- 

החלוקה לצוותים על ידי צוות אודיסאה:  
   
כל צוות צריך להוסיף למאגר הקוד \- bitbucket קובץ README.txt המכיל את שמות חברי הצוות וכתובת של שרת שמופעל על אחד המחשבים בבית.  
לא ניתן יהיה להמשיך לפני שהמנחה לא יוכל לקרא את הקובץ דרך github/bitbucket  
ויוכל להתחבר אל השרת מהבית או מהכיתה.

## שלב \- הצגת מצב הרשת

יש לכתוב תוכנית המתחברת אל אחד השרתים ברשת (למשל זה של המרצה) ושואבת ממנו את רשימת הצמתים ואת ה-block-chain  ומציגה אותם. תוספת \- להציג ליד כל בלוק לא רק מספר ארנק \- גם שם ארנק אם ניתן להסיק. תוספת נוספת \- לסכם את כמות המטבעות שיש לכל ארנק ולהציג ממויינת.

## שלב \- פרסום מבנה הרשת \- ניסוי

כל ההודעות שמשודרות ונקלטות בכל מהלך הפרוייקט (בכל השלבים) הן באותו פורמט בינארי.  
בונוס 1: רשת מבוזרת \[  
כל צומת (שרת) משדר בקשה ומקבל תשובה משלושה צמתים אקראיים (מרשימת הנוכחות nodes) באחד מהתנאים הבאים:

1. היה שינוי ברשימת הנוכחות (לא כולל שינוי בזמן)  
2. היה שינוי ברשימת הבלוקים (רלוונטי רק לשלב הבא \- כריית מטבעות)  
3. עברו 5 דקות ללא שינוי

\] (סוף בונוס 1\)  
בעת שידור ההודעה יש לסמן בשדה cmd=1  
הצומת הקולט מחזיר הודעה עם הרשימות המעודכנות ומסמן cmd=2

Request/Response structure

| שם שדה | גודל bytes | format | חוזר? | הערה |
| ----- | ----- | :---- | ----- | ----- |
| cmd | 4 | int32 big-endian |  | value request=1 or response=2 |
| start\_nodes | 4 | int32 big-endian |  | always 0xBeefBeef |
| nodes\_count | 4 | int32 big-endian |  | can be zero or more |
| nodes | 8 bytes or more | see Node below | nodes\_count |  |
| start\_blocks | 4 | int32 big-endian |  | always 0xDeadDead |
| blocks\_count | 4 | int32 big-endian |  | can be zero or more |
| blocks | 36 | see Block below | blocks\_count |  |

הבקשה והתשובה מכילות בתוכן תת מבנים מסוג Node, Block  
מבנה מסוג Block הוא בעל גודל קבוע של 36 בתים

### מבנה Node

מבנה זה משמש ל״רשימת נוכחות״ של המחשבים המשתתפים ברשת.  
מבנה מסוג Node הוא בעל גודל משתנה, האורך המינימאלי (אם האורכים של ה-strings הם אפס) הוא 8 בתים.  
Node structure

| שם שדה | גודל bytes | format | חוזר? | הערה |
| ----- | ----: | :---- | ----- | ----- |
| name\_len | 1 | int8 |  |  |
| name | 1 | char ASCII | name\_len | team-name |
| host\_len | 1 | int8 |  |  |
| host | 1 | char ASCII | host\_len |  |
| port | 2 | int16 big-endian |  |  |
| last\_seen\_ts | 4 | int32 big-endian unix timestamp |  |  |

השדה name הוא string באורך name\_len המסמל את שם הצוות המפעיל את הצומת. שימו לב שאותו צוות יכול להפעיל כמה צמתים.  
השדה host הוא string באורך host\_len המסמל את כתובת ה-IP או שם DNS של השרת. שימו לב שאותה כתובת יכולה לשמש עבור שני צמתים \- עם port שונה \- זה שימושי במקרה של הרצות ניסוי.

השדה last\_seen\_ts מכיל את הזמן בו נראה שרת זה לאחרונה. מי שממלא אותו הוא השרת עצמו לגבי עצמו. כל צומת (שרת) שמאחד רשימות הנוכחות צריך לקחת את הזמן האחרון ביותר (המקסימאלי) מבין שתי הרשימות \- זאת שקיבל וזאת שאצלו בזכרון. הדרך לדעת מה הזמן הנוכחי ב Java היא  
int current\_timestamp \= (**int**)(System.*currentTimeMillis*() / 1000);

המספר הזה מקובל למדידת זמן והוא מספר השניות שעברו מאז ה 1 בינואר 1970 בחצות הלילה בגריניץ׳, אנגליה.  
צומת שלא נראה במשך יותר מחצי שעה (30\*60 שניות) \- צריך להמחק מרשימת הנוכחות. 

### אופן החלפת רשימת הנוכחות

החלפת רשימות הנוכחות מתבצעת כחלק מהחלפת הבקשות בין צמתים.  
כל צומת מחזיק ברשימה לפחות את הנוכחות שלו. כאשר צומת שולח את רשימת הנוכחות עליו לעדכן הזמן של עצמו ל״עכשיו״  
רשימת הנוכחות בזכרון צריכה להיות HashMap שהמפתח אליו הוא הזוג (host, port) \- זוג הוא Record והערך הוא שאר השדות \- שם , זמן נוכחות אחרון ועוד שדות נדרשים. יכולים להיות שדות נוספים עבור כל node שלא נשלחים ברשימה אך השרת צריך לתחזק \- לדוגמה שדה ״מצב פעילות״ \- ראה בהמשך.  
זה שהמפתח מכיל גם port מאפשר להריץ כמה שרתים על אותו מחשב לצרכי בדיקות.  
כאשר קולטים רשימה חדשה מהודעה מצומת אחר יש לאחד את הרשימות לפי הכללים הבאים

1. צומת שהגיע בהודעה ולא נמצא בזכרון \- יש להוסיפו לרשימה בזכרון ב״מצב פעילות״=״חדש״ \- יש לסמן שהיה שינוי\!  
2. צומת שהגיע בהודעה וגם נמצא בזכרון \- יש לשמור עבורו את הזמן המקסימאלי מבין השניים. אם הזמן שמופיע גדול מהזמן הנוכחי (עתיד) \- יש לשים את הזמן הנוכחי (מונע הטעיות מכוונות)  
3. צומת נשאר ב״מצב פעילות״ \= ״חדש״ כל עוד לא שלחת (מיזמתך) אליו הודעה וקיבלת תשובה. צמתים שהם במצב פעילות חדש לא ישלחו ברשימת הנוכחות הלאה. זה נועד למנוע התקפה של התחברות אל השרת שלנו ומתן רשימה מזוייפת עם כתובות שאינן מכילות מחשבים פעילים.

לאחר מכן יש לעבור על הרשימה ולמחוק צמתים שלא נראו במשך יותר מחצי שעה (מותר לבצע מחיקה מרוכזת פעם בחמש דקות).

### מבנה Block \- שלב החלפת רשימות

בשלב זה לא יוחלפו בלוקים \- לכן השדה blocks\_count יהיה אפס תמיד. לתיעוד מבנה זה \- ראה שלב כריית מטבעות- ניסוי, בהמשך.

## שלב \- פרסום מבנה הרשת \+ שמירה \- ניסוי

בשלב זה ישמור השרת את כל הרשימות במבנה של בקשה עם cmd=1 לקובץ בינארי כל פעם שיש רשימה ברשימת הצמתים או ברשימת הבלוקים. יש לשמור פעם ב-5 דקות.  
כשהשרת מתחיל \- עליו לקרא קובץ זה ולבנות את הרשימות בזכרון  
שלב זה נועד שניתן יהיה לעצור את השרת ולהפעיל אותו מחדש והוא ימשיך בפעולת הכריה.  
שימו לב שבאופן טבעי, אם כיביתי את המחשב או עצרתי את השרת למשך יותר מחצי שעה \- כאשר נפעיל אותו מחדש \- הוא יתעלם מכל השרתים שנראו לפני יותר מחצי שעה \- ולכן הרשימה תהיה ריקה.   
(רשימת הבלוקים איננה נמחקת לעומת זאת)

## שלב \- פרסום מבנה הרשת תחרות up-time למשך 4 ימים

בשלב זה כל הצוותים יריצו שרתים ונבדוק אלו צוותים קיים עבורם לפחות שרת אחד פעיל לאורך הכי הרבה זמן. המדידה תעשה על ידי השרת של המרצה שישמור נתוני נוכחות לקובץ.  
צוותים שלא יצליחו להחזיק שרת באויר למשך יותר ממחצית זמן התחרות \- לא יוכלו להמשיך  
הקוד יוגש דרך bitbucket על ידי סימון tag ושליחת קישור למרצה.

## שלב \- כריית מטבעות \- ניסוי

בשלב זה יתווספו בלוקים עם מספרי הארנקים שזכו במטבע.  
הבלוק הראשוני \- ״בלוק בראשית״ \-ינתן לכולם.  
הבלוק הראשוני יהיה שונה בשלב כריית הניסוי ובשלב כריית התחרות בכדי שלא יהיה ניתן להשתמש ברשימת הבלוקים מהניסוי עבור שלב התחרות.  
כל הקוד שקשור לבלוקים נמצא במחלקות HanukCoinUtils, Block

Block structure

| שם שדה | גודל bytes | format | חוזר? | הערה |
| ----- | ----: | :---- | ----- | ----- |
| serial\_number | 4 | int32 big-endian |  | must be \+1 from previous block |
| wallet | 4 | int32 big-endian |  | Wallet that won a coin |
| prev\_sig | 8 | byte \[8\] |  | the first half of MD5(previous\_block)  |
| puzzle | 8 | byte \[8\] |  | 8 bytes that make the MD5 of this block end with zeros as required |
| sig | 12 | byte \[12\] |  | MD5 signature of this block \- first 12 bytes out 16\. |

בשלב זה יופעלו השרתים של הצוותים. יש לדעת לשמור לדיסק גם את רשימת הבלוקים לקובץ.  
על הצוותים להפעיל שרת שיהיה מחצית מהזמן פעיל.  
צוותים שלא יצליחו לכרות לפחות שני מטבעות לא יוכלו להמשיך.

### החלפת רשימות בלוקים

בכל שינוי ברשימת הנוכחות או ברשימת הבלוקים \- יש לבחור שלושה צמתים אקראיים מהרשימה ולשלוח אליהם בקשה ולקבל תשובה. שרת שישלח בקשות רק לשרת המרצה \- יפסל.  
בכל פעם שמקבלים רשימה יש לעבור ולבדוק את התקינות של רשימת הבלוקים שהתקבלה.  
אם רשימת הבלוקים שהתקבלה אינה תקינה \- יש להתעלם ממנה.  
אם רשימת הבלוקים שהתקבלה ארוכה יותר  ממה שיש לשרת בזכרון \- יש לזכור את הארוכה יותר וכן לשמור אותה לדיסק.  
אם רשימת הבלוקים בדיוק באותו אורך \- יש לקחת את זאת ששדה ה puzzle בבלוק באחרון הכי קטן (קטן לקסיקלית כשמסתכלים על הבתים מההתחלה לסוף). (הסיבה לכך היא שאם שניים כורים מטבע באותו זמן והרשימות שלהם באורך זהה \- אף אחד מהם לא ימשיך לכרות בגלל המגבלה על שני בלוקים עם ארנק זהה וכמו כן אף אחד מהם לא מחזיק רשימה ארוכה יותר אז בלי תנאי זה הרשת יכולה להיתקע)  
במקרה של הפעלת השרת מחדש \- יש להעלות מהדיסק את הרשימה ולבצע לה בדיקת תקינות.

אופן בדיקת התקינות לרשימת הבלוקים

1. הבלוק הראשון \- מספרו 0 והוא חייב להיות זהה ל״בלוק הבראשית״  
2. מספר serial\_number של כל בלוק גדול ב 1 מקודמו.  
3. עבור כל בלוק יש לחשב את החתימה MD5 על כל הבלוק פרט לשדה sig. לגבי החתימה יש לבדוק את שני הדברים הבאים \-    
4. החתימה תואמת את השדה sig  
5. החתימה מסתיימת ברצף של אפסים באורך NZ או יותר. המספר NZ תלוי במספר הסידורי של הבלוק. ערכו NZ=20+NumBits(serial\_number)); NumBits(k)=1+floor(Log2(k))R  
6. מספר הארנק בבלוק הזה אינו זהה למספר הארנק בבלוק הקודם. תנאי זה גורם לכך שאף ארנק לא יכול לזכות ביותר ממחצית מספר המטבעות.

### כריית מטבעות

בכדי להרוויח מטבע יש להוסיף בלוק עם מספר הארנק ״שלך״ (של הצוות) אל רשימת הבלוקים.  
בכדי לעשות זאת יש להוסיף בלוק תקין לרשימה.  
החלק ה״קשה״ הוא מציאת ערך לשדה puzzle כך שהחתימה של הבלוק תסתיים במספר אפסים כנדרש לבלוק תקין.  
תהליך הכרייה כולל מעבר בלולאה על הרבה ערכים אפשריים של puzzle ואז חישוב חתימה.  
כל זה צריך להתבצע ״במקביל״ לקבלה ושידור של הודעות. כלומר צריך כל פעם לנסות כמה אפשרויות לכריית מטבע ואז להמשיך לטפל בתקשורת (אם משתמשים ב thread/process יחיד)

## שלב \- כריית מטבעות \- תחרות למשך שבוע

בשלב זה יחולק בלוק התחלתי (בלוק בראשית) חדש ותתחיל מהתחלה הכריה ובניית רשימת הבלוקים.  
לכל צוות מותר להריץ עד 3 שרתים \- צוות שיריץ יותר שרתים \- יפסל.  
התחרות תארך 7 ימים וכל צוות יקבל ניקוד שתלוי גם במספר המטבעות שיאסוף.

## שלב \- הגשת הקוד

הקוד יוגש דרך bitbucket ויסומן ב tag שאותו המרצה יבדוק.

## חידון

* מה גודל בקשה/תשובה הכי קטנה  
* אני מחובר לרשת הביתית. כתובת המחשב שלי ברשת הפנימית 10.0.0.14 \- מה אני צריך לכתוב ברשימת הנוכחות בתור הכתובת והפורט שלי?  
* לצורך ניסוי פנימי שלי \- אני רוצה להאיץ את יצור מפתחות \- איזה פונקציה צריך לשנות?  
* אני רוצה להריץ שלושה שרתים לצרכי ניסוי על הלפטופ שלי \- איך אפשר?  
* האם מתקפת DDOS תגרום שלא אכרה מטבעות לגמרי?

## ציונים

**50% \- קידוד**  
שימוש בפונקציות  
רמות קינון  
הערות ותיעוד  
קריאות ומובנות של הקוד  
שימוש ב objects/threads וכדומה \- רק אם משפר או לא פוגע במובנות של התוכנית  
פונקציות ניתנות לבדיקה ודרך לבדוק אותן \- בונוס

**50% \- הפעלה**  
עבודה בלי באגים  
יציבות  
10% כמות מטבעות \- יחסי לאחרים אך לא בהכרח לינארי

  מי שיכתוב שרת פשוט עובד ויציב עם קוד קריא \- גם אם יכרה הכי מעט בכתה \- יקבל 90%

**עמידה בזמנים**  \- יהיו הורדות ציון לפי הצורך

## נספח 1 \- דוגמאות קוד

פונקציות לניהול בלוקים וכריה:  
[https://github.com/tal-franji/HanukCoin](https://github.com/tal-franji/HanukCoin)

# שרתים לתחרות

מסלול GPU  
נמצא ב [http://34.165.24.48:8080/](http://34.165.24.48:8080/)  
בלוק בראשית לתחרות GPU

Block gpu \= new Block();  
gpu.data \= HanukCoinUtils.*parseByteStr*(  
       "00 00 00 00  00 00 00 00  \\n" \+  
               "47 50 55 43  4F 4E 54 45  \\n" \+  
               "05 78 3D 48  B7 57 5C 47  \\n" \+  
               "BF 9F BD 8A  55 57 F1 7A  \\n" \+  
               "2D 43 37 F7 ");

מסלול CPU  
נמצא ב[http://35.239.122.216:8080/](http://35.239.122.216:8080/)

בלוק בראשית לתחרות CPU  
Block cpu \= new Block();  
cpu.data \= HanukCoinUtils.*parseByteStr*(  
       "00 00 00 00  00 00 00 00  \\n" \+  
               "43 50 55 43  4F 4E 54 45  \\n" \+  
               "2A D2 36 CA  05 7A 0F C6  \\n" \+  
               "25 03 F4 9E  7C B6 C8 93  \\n" \+  
               "4F EE 59 92 ");

# תוצאות התחרות

![][image1]  
![][image2]

## נספח 2 \- בלוק בראשית \- הבלוק הראשון בשרשרת

נקרא גם  genesis block

**static private byte**\[\] parseByteStr(String s) {  
   ArrayList\<Byte\> a \= **new** ArrayList\<Byte\>();  
   **for** (String hex : s.split(**"\\\\s+"**)) {  
       **byte** b \= (**byte**) Integer.*parseInt*(hex, 16);  
       a.add(b);  
   }  
   **byte**\[\] result \= **new byte**\[a.size()\];  
   **for**(**int** i \= 0; i \< a.size(); i++) {  
       result\[i\] \= a.get(i);  
   }  
   **return** result;  
}

**public static** Block createBlock0forTestStage() {  
   Block g \= **new** Block();  
        g.data \= parseByteStr("00 00 00 00  00 00 00 00  \\n" \+  
                        "43 4F 4E 54  45 53 54 30  \\n" \+  
                        "6C E4 BA AA  70 1C E0 FC  \\n" \+  
                        "4B 72 9D 93  A2 28 FB 27  \\n" \+  
                        "4D 11 E7 25 ");

   **return** g;  
}

[image1]: <data:image/png;base64,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>

[image2]: <data:image/png;base64,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yeFcghZASaDYBS0AACh5ELTArxC0AADAcwha4FcIWgAA4DkELfArYYOW/o9BERERL5S62Lj7EP1g2KClnYiIiIh4/hK0EBERET2SoIWIiN6YHaYP0WcStBARERE9kqCFiIiI6JFFBi3di+r66683ewyWK1dOnnvuOfn888/DztPymmuukYYNG5q9D91zwukch4iI/lG/+x2dvsaNG5syOztb6tevL5UqVQo5DjHajChoBdadoKWbftatW9deCM68RYsWmTIlJcWUq1atktq1a0vp0qXtvFKlStn5Wu7YsYPAhYjoE/U7f+LEifLII4+YUKV9X3zxhQ1aur/tp59+KnPmzAk5FjHajChoOT7wwAM2aDnBaOXKlXaelvXq1ZMmTZrYAKUbQGv//Pnzg+Y9++yzkp6eHvKLBhERS7YbN26UNWvWSEZGRtD3vxO01q1bJy1atDDriftYxGgzoqAV2HYHLfc8546Whih95Ni9e3fp3bu3bN68OWje888/L8uWLTNtfdSodfdnIyJiyfObb76x9XBBq23btravS5cuIccjRpPnHLT0TpW+t3XXXXcFzXPuUDnt/v37S506dSQ1NVWysrLCBi1tJyQkhHw2IiKWTMuUKSPly5eX2bNn2z4naOlaoe8EO6+cIEazRQatM5mUlGTK6667LmQMEREREc8jaCEiIiLir0vQQkRERPRIghYiIiKiRxK0EBERET2SoIWIiIjokSFBCwAA4ELiLDYAfoOgBQAAnkPQAr9C0AIAAM8haIFfIWgBAIDnELTAr0QctObOnSu33HKLvPnmm+4hAACAXyVc0Bo3bpwMHz7ctj/++GMZO3asbevYpEmTbBsgGokoaMXFxUlubq6pHz161Oy6DgAAECnuoLVr1y5b1/1uO3ToIMeOHZO8vDyzP+7o0aPNWGFhoZ0HEI1EFLT0InDTp08fadSokRQUFMiIESNMX7h5AAAA7qDlcOLECalcubKULVtW6tatazaRdvjoo48kISEhYDZA9HFWQSs/P984cuRIE7RycnJMv3Prl6AFAADhCBe0OnfuLA899JCpa9By7l7VqlUrYJbYH/MA0UhEQatp06b2Nu7atWtl1KhRQUGrevXq8vPPPxO0AAAgLO6gpa+j6Dtap0+fNvbr10+WLFki27Ztk65du8rEiRPND3t9nKjrDkC0ElHQUtLS0qRBgwbyl7/8xbSdC0TR0DVkyBC57bbbAg8BAAAwuIPW888/b9YMR+Xxxx+XTp062TktWrSQNm3a2DZANBJx0AIACIJ3lOEscActAL9A0AIAAM8haIFfIWgBAIDnELTArxC0AADAcwha4FcIWgAA4DkELfArYYOWdiIiIiLi+UvQQkRERPRIghZi1Jkdpg8REYujBC1EREREjyRoISIiInrkrwatzZs3S/PmzW1b9zN0n8DtjBkzQvoQEREdt2/fLrfffrv0799f6tevb/qqVKkiLVu2lOeee87Oi2TNQSzu/mrQUp1/6NnZ2VKxYkVZsWKF6UtNTTX9GRkZpt2lSxf505/+ZOeXLl1aSpUqJevXrzf7V+lFpce6z4+IiP5y+fLltq5rxtKlS2X16tWyZcsW25+QkCCXXXZZyLGI0WaRQWvOnDkyffp0+6tDL4rFixcbnZCl/atWrTJhTO9oDR061B6v4x07dpT09PSQcyMion9NTEyUtWvXyvDhw80TlE8//VQaNWokkydPNusJQQtLgkUGLVXDUlxcnK3rrxE1KyvLBi29WLStQevJJ58MOlaDVmZmZsh5ERHRn+rasGnTJlN/8803bX/lypXNmGOvXr1CjkWMJiMKWvXq1ZPXXnvN1FNSUmTQoEFyww03mF8cTZs2Nb8+9DGhjteqVcuUN910k/Tr109q1KhB0EJERKs+Kbn++uvNO8Dq+PHj5brrrpOaNWvKvHnz7DzuaGFJMKKghYiIiIhnL0ELERER0SMJWoiIiIgeSdBCRERE9EiCFiIiIqJHErQQERERPTIkaAEAAFxInMUGwG8QtAAAwHMIWuBXCFoAAOA5BC3wKwQtAADwHIIW+JWIgtbx48elYcOGpl5QUCCjR4+2Y40bNzZly5Yt5fbbb7f999xzj/z5z3+27TvvvFNatGhh28qsWbPkyJEj9hxni27fAAAAxZ9wQatt27bSrFmzoLZu7xbIHXfcEdQGiDYiClq6qacSExNj28eOHZNp06ZJXl6e7N+/X3788UfJycmRqlWrSp8+fWTLli1mL6sNGzbIkiVLzJiGqkDGjh0rBw8etOc/W2JjY91dAABQDHEHrcOHD5t1RH+86xqge+PqejBz5kxZvHixmfPVV1+d8/oAUFyIKGj179/flLrZp4OGHN0wWklLS7P9elFUrFjRtuvWrWv6dMNp54Jxyvj4eBu0Tp8+be56BY7rDu75+flmXmC/E7C4AAEAogN30HLYvXu3XHXVVbYd+L0+ZswYufzyy20bIBqJKGj17dvXlKmpqabMzc2VMmXKBF0QGn40YLmDVr169YLmnTx50galyZMnh72jpW3H1atXB7X1D9aLT4mLiws6DgAAiifhgtbGjRulfPnyQX3Z2dly8803m/Cl64OuO/r6CkC0ElHQcoJQ4KND5eeff5Znn33W3PrV8LV582bp0KGDjBw5Uj788EMZMmSIeYQ4f/58M3fdunX2+MLCQklJSbFBS9s9evQIOn/9+vXNY8mEhISgfvcdLgAAKN64g5aGpw8++EBOnDhhbNCggWRmZsq8efPkwQcftPO4owXRTkRBSx/fPfnkkyYM6W1eDVUOU6ZMMeVzzz1n68obb7wh77//vm3r+1gTJkyw7aFDh0pGRoacOnXKHPfCCy/IwoULzZh+jj6u1Pe7FH3/a8CAAeZiVL7//nt56aWXZOrUqfZ8AABQfHEHLX0PS7/7HZWJEyfK7Nmzg+a52wDRRkRBCwAA4HxwBy0Av0DQAgAAzyFogV8haAEAgOcQtMCvELQAAMBzCFrgVwhaUAwodHcAQAmDoAV+JWzQ0k5EREREPH8JWoiIiIgeSdBCRERE9EiCFiIiIqJHErQQERERPbLIoLVp0yazr2DdunXtxs5unf6qVauGjCEiIrrVfWzLlSsnX375pdmSTdcRVffNdc9FjGaLDFrucKV7EGqpF4heKIFzNGjpZtC6A/uaNWtk0KBBZl/DSpUqmbm6Qaj7fIiI6E81YLVs2VLee+89087KypIbb7wxZB5iNBtx0Grfvr1RN/689tprzQXijLuD1o4dO2T16tVmM+ohQ4aYsXHjxpny3//+t4wcOTLkcxAR0T/qZtK6dowZM8b2xcbGyvbt20Pmokdmh+nDC27EQctR71IlJSUFjbuDll4oy5cvDwpaM2fONOW2bdvkmWeeCfkcRET0h2lpabburB/utQaxpFhk0NJHhXoBOO9orV27Vj777DNTL1WqlCxdujQoaE2ePNm09ZcJQQsREd3q6yU1atSQRo0aSZcuXaRs2bL2R7uuNe75iNFskUELEREREc9NghYiIiKiRxK0ELHEmh2mDxHxYkrQQkRERPRIghYiIiKiR4YErX379iEiIl4wdbFx9yH6wbBBCwAA4ELiLDYAfoOgBQAAnkPQAr9C0AIAAM8haIFfIWgBAIDnELTAr0QUtHbt2mW2RTh58qQUFBTIH/7wBzum/U6p5uTk2LFAdKxJkyamXlhYaNojRoyw46dPn5bXX3/dznVcv369fPjhh6a+d+9eM75ixQrTPnHihD0eAACKL+GCln6Ply5d2qwJyqWXXipt27YNngQQ5UQUtALDlJKYmCjff/+92a9QL5CjR4+GzA2kX79+ptRNphVnzv3332+C26JFi8zF5gQtB2ee7ocV2E5ISDBlTEzMLxMBAKBY4w5aBw8etPWrr75annjiCfn5559l3bp18uqrrwbMBIhuIgpaDz/8sCnj4uJsn4aeihUr2raid6U0MOkm1NWrV5cqVarIkSNHzNihQ4dMn9K7d28ZNWqUDU47duwwZWDQuuOOOyQrK8u2FZ2vwa5nz56mXaZMmaBxAAAonriDlpKbmyt9+vSRMWPGSIUKFWx/vXr1AmYBRDcRBa1HHnnElE6wOnz4sCQnJwfdvdJfIHXq1DH12NhY++ivWrVqdo4+etTHfc5xkyZNkjlz5tjxwKClO7k7OI8aHUaPHm3aemcNAACKP+GCloN+n5cvX96269evHzAKEN1EFLTcjw6dcv78+eZ/xqX87ne/+2Wy/PKo79SpU0a9G9agQQPTn5+fb37BOMd//PHHMm3aNHtcYND65JNPbF3n6yNGh5tvvtmUPDoEAIgO3EFry5Yttq7f8b/97W/ND/HMzEwZPHhwwEyA6CaioKV3lP75z3+ael5enn1xUdHn7HqHS0tHp19fondYvHixea/LYdOmTXLs2DHbVpwX6fX87s8IPLeGtWXLltlxAAAo3riDlpKRkREUuHQtCVwnAEoCEQUtAACA8yFc0ALwAwQtgGLI/9/PBSgZELTArxC0AADAcwha4FcIWgAA4DkELfArBC0AAPAcghb4lbBBSzsRERER8fwlaCEiIiJ6JEELERER0SMJWoiIiIgeSdBCLPFmh+lDRMSLYZFBq3bt2tK+fXtp166dacfFxdmxpk2bmrJcuXJy+eWXy8yZM+2Y7sCu5Zo1a8zxZcqUsZtN6wbVzryrrrpKsrKyZN68edKiRQsz7nyuM9+Ze99998lTTz0V8jciImJ06Xy/B37HI5ZEiwxazZs3N6VzMXzzzTdSqlQp23700UftXKdv4cKFNmi5L6TY2FhTDho0yPQnJiaaoKX1zZs3B51r/Pjxtt2/f3/57LPPCFqIiFFudna2LF++XP7+97+HjCGWNIsMWj179jRl3bp1bV98fHxQ4Pnb3/5mNp4ODFRO0NINpZ2+wHGnnpqaaoNWYH9MTExQu3Xr1rJ161aCFiJilKtPOpYtW2a+07mjhSXdIoPWLbfcYkrnYnjsscdk4MCBNgip27ZtM79QziZopaSkmLJly5ZBQcuZv337djuelpZm74xxUSIiRre6Xjh1vtOxpFtk0NI7WR06dDDvT2VkZEiPHj3smHOB6PtUZcuWNY/2nLFwQUvvXjn1Nm3ayE033WQfHY4dO1batm1rz6nvfXXt2lXq1Kljj+GOFiJi9KtBq3LlylKjRg159tlnQ8YRS5JFBi1EREREPDcJWoiIiIgeSdBCRERE9EiCFiIiIqJHErQQERERw5gdpu9sJWghIiIiemRI0AIAALiQOIsNgN8gaAEAgOcQtMCvELQAAMBzCFrgVwhaAADgOQQt8CsRB629e/fKsGHDJDc31z0UllOnTrm7fpUDBw7Ihg0b3N0R8/HHH7u7AACgmBAuaOkeuRs3bnR3A5QoIgpapUqVsgGrXbt2tv+HH36wdeX777+3dd2v0CHcBaYUFhbaus6Pj48PGBXZvXu3Kd2fo6HMwR383AFv586dQW0AALj4uNeBffv22foVV1wRMAJQsogoaAWGpsC+1atX2zEt169fb0r9haLl0aNHzQW0atUqqV69etCxjz32mNmEOj8/XwoKCuTOO++Uf/3rX0FzPvroI1N++umnEhMTI7t27ZLrrrtOFi1aJOPHj5f9+/fLrbfeasYrVKggTzzxhDz66KNSpUoVc44mTZrIihUrwv79AABw8XAHLeXgwYPSunVrmTt3rnsIoMRwzkHr0KFD8swzz9ix7t27m9J5hOf0L1myRP74xz8GncOpT5o0yfwBunu71qdPn26CkjJv3ryguVrqBdmjRw+jtjVoOWjQcv+dX3zxhQl07n4AALi4hAta+kNb+/mOhpJMREGrYsWK9nFduXLlzCO/rl27mrZzgVSrVs2U9957r+0fPHiwuZAC5wXWp0yZEnKROXW9axXY1rJ///7y5ZdfmnZSUlJI0NK7Zvp5X331lSxdutSOcxEDAPxvcQctfULhwHc0lGQiClpKixYtzMXw7bffmnb9+vVNu1mzZqZ9zz33mPa0adNMu3bt2uaRoPaVL18+bJiaOnWqOV/ge1SvvPKKvPzyy7J48eKguU7Ztm1bU9ewp7edHTR4KYmJidK+fXt7jPY7d9sAooL/f3URoMTgDlqKfkereXl57iGAEkPEQasoHnjgAXcXAACAIVzQAvADFyxoAQAAnAmCFvgVghYAAHgOQQv8CkELAAA8p7gHLV6NBK8gaAEAgOcU96AF4BVhg5Z2IiIiIuL5S9BCRERE9EiCFiIiIqJHErQQERERPZKghYiIiOiRRQYt3etwwIABZi9BbcfExNixjh07mjI5OVmuuOIKmTBhQsjxiIiIbrdu3Wq24HH3I5Y0iwxalStXNqVzQYwdO1batWtnA1d6erqdy0WDiIiRqD/OWTPQDxYZtHr16mXKq6++2vbFxsZKt27dTL1v3762n4sGEREjlTUD/WCRQatJkyamdC6ITp06yYsvvigVKlQw7XfeecfO5aJBRMRIZc1AP1hk0NK7V5988okkJCSYdmpqqh27/PLLTdm9e3cZOnSo9OzZM+R4RETEcBK00A8WGbQQERER8dwkaCEiIiJ6JEELERER0SMJWoiIiIgeSdBCRERE9EiCFiIiIqJHhgQtgP8the4OAIhynMUGwG8QtAAAwHMIWuBXCFoAAOA5BC3wKwQtAADwHIIW+JWIglZhYaH0799fBg8e7B4CAAAokjMFrccff9zdBVCiiCho6V5UBQUFsnfvXqlUqZJ7GAAA4FcJF7RuvPFGs74AlGQiClo1atSQ5ORkycjIMO0JEyZIlSpV5NChQ3LTTTeZEDZnzhz5+uuvpVatWvL73/9exo8fL71795bZs2fLlClT5L///a80btzYHK9hbcuWLXLllVdKXl6eudCWLl0qR44ckQEDBsjkyZPl4YcflrS0NOnTp48kJiaa41u1aiW7du2SZs2ayfbt2+Waa64J/DMBAKCYEi5oKQQtKOlEFLQUDVNDhgyR2NhYE7T27Nlj+vUiad68uZ2n7ZiYGNtOSUkJuZC0XbNmTalWrZoMGzbMjv/000+21MeVSqNGjUz97rvvltKlS8uaNWtM0AIAgOiBoAV+JaKgFRic9KLQoLVy5UrbHjt2bNB4UlKSqefn55tA5VxIevfKmaPs379fNm3aFDZoOWjQ0nENWyNGjJAVK1YQtADOBf73ZPA/hKAFfiWioKW0aNFCWrZsaeoatN555x1p0qSJHb/jjjuC7mzddddd0r17d9u+/vrrZcGCBbatjwH1Dply6623mvLEiROmPH78uJ2njw43btwoDRs2lFOnTsm4ceOkX79+dhwAAIo/Zwpazvc/QEkl4qAViAatHTt2uLsBAADCcqagBVDSOaegBQAAcDYQtMCvELQAAMBzCFrgVwhaAADgOQQt8CsELQAA8ByCFvgVghYAAACAxxC0AAAAADyCoAUAAADgEQQtAAAAAI8gaAEAAAB4BEELAAAAwCMIWgAAAAAe8X/VGEo//hsk1gAAAABJRU5ErkJggg==>
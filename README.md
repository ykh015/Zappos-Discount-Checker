Zappos-Discount-Checker
=======================

An Android application that uses Zappos API and lets a user pick their desired product(s) and then notifies them when the price hit at least 20% off the original price

This portfolio  contains the screenshots of the app :
https://www.behance.net/ykh015

**Description of the Files:**

1. *DiscountChecker.java*
It is the first activity which starts whenever the app is started. It checks for the discount of the favorited products (products in the database). Checks the percentoff for every favorited item matching the ProductId and StyleId.
If the discount is greater than or equal to 20%, the user gets a notification.
2. *Category.java*
After sending notifications, the categories of the products are shown. 
In this app only 5 categories are being used : Boots, Kettles, Lipsticks, Shoes and Jeans.
3. *Product_List.java*
When the user selects a category, all products are shown in that category.
4. *SingleProduct.java*
When a user clicks on a particular product, the name, brand name, price, original price, discount and image are shown.
The user ha an option to "Mark as Favorite" an item. When he clicks on that, the item is stored in the database.
If the item was previously marked as favorite, then the user can "Remove as Favorite" an item. After clicking that, that particular product is removed from the database.
5. *DiscountProduct.java*
It is similar to SingleProduct.java. It is called when the user clicks on a particular notification.
6. *DatabaseHandler.java*
It contains all the database related operations.






**Furure Improvements:**

1. Currently the app checks the discount when it is started. Later, we can create a service in the background which will periodically check the dicounts of the items and then send notifications.
2. The API key is present in DiscountCheker.java and Product_List.java. We can create a string resource for the API key.
3. The user interface can be vastly improved.


**Extras**

1. Used GSON for JSON parsing
2. APK file can be downloaded from the link

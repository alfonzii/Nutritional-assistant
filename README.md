# Nutritional-assistant
Simple android application for nutrition control with adaptive meal plan generation created in Java with Android SDK.

## User manual

### Main activity
On main screen you see overview of your daily food intake. You got three options from main:

1. Add food (floating button)
2. Reset your daily income
3. Settings

#### Add food
When you click food adding button, application get you to an activity, where you have search bar.
You can start typing and search bar will provide you with suggestions to what are you writing.
After you choose what you want to eat or have eaten, dialog window will pop up on you with number picker to choose grams of your food
and you can add this food to your daily income.

#### Reset your daily income
From main screen you got option (in action bar) to reset your daily food income. Simply press the button and you are back to 0s.

### Settings
Clicking Settings action button presents you with 4 options:

1. Nutrition settings
2. Database (Work in progress)
3. Intake overview (TODO)
4. User parameters

#### Nutrition settings
In nutrition settings you can set your daily nutrition limits. You can choose from manual or automatic setting.
From manual you can set limits manually, but most of time, automatic is sufficent. If you choose automatic, then you are asked about your current lifestyle
(sedentary, mild activity, medium activity, high activity) and what is your fitness goal (gain, lose, maintain). After entering these, app calculates you your daily limit calories.
Prerequisite to this automatic calculation is entering your parameters in User parameters acitivty. If you haven't entered them, app won't let you do automatic calculation
and will ask you to enter them.

#### Database (Work in progress)
You can filter foods by calories, fats, carbohydrates and proteins. From given filter variables, database presents you with food sufficing these constrains.

#### Intake overview (TODO)
Here you will be able to see your intake graphs in long term and check on how you keep to your diet.
Again, there will be more categories (calories, fats, carbohydrates, proteins).

#### User parameters
To app function correctly when using automatic nutrition settings, you are required to enter your physical parameters, here.

### Minimum API Version
* 23 (Android 6.0 Marshmallow)

## TODO
* Implement Database
* Implement Intake overview
* Use [View Binding](https://developer.android.com/topic/libraries/view-binding) :heavy_check_mark:
* Make activity hierarchy in AndroidManifest.xml to be able to use "back" action button
* In food adding dialog popup change number picker to number entering (number picker was used just for personal learning purposes)
* Use external solution with API for foods (right now I'm using just local database with cca. 1K of entries)

## Tested on devices
* Nexus 5X (API 29, x86 emulator)
* Huawei P8 Lite (Android 6.0, API 23, physical device)

## Database
Powered by [Nutritionix](https://www.nutritionix.com/business/api) & [Spoonacular](https://spoonacular.com/food-api) API

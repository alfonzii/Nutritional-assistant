# Nutritional-assistant
Simple android application for __nutrition control__ and __weight goal__ achievement with __adaptive meal plan generation__ created in _Java_ with _Android SDK_.

__Biggest asset__ of this application in comparison to similar nutrition control/manager apps on market is that users __don't have to strictly obey given meal plan__. Instead, they can __deviate__ from it if needed and still be able to __meet__ their nutrition __limits__ and goals by __using__ application's __adaptive meal plan re-generation__.

## Quick overview
Application helps it's users to get dreamed weight in relatively painless, straightforward and healthy way. User provides 6 parameters (sex, age, weight, height, current lifestyle, weight goal), from which are daily nutrition intake limits calculated to meet chosen weight goal. Afterwards, user can generate daily meal plan that many times as he needs, to be finally happy with the one generated (beware of [API request limit](#api-request-limit) in this version of app). Generated meal plan satisfies nutrition limits as much as possible. Application also supports adding foods from provided databases manually. Adding food manually breaks generated meal plan, because it changes assumed nutrition intake. User can deal with such sitations by using application's adaptive re-generation of meal plan, so it's now possible for user to add his own foods to meals and be sure that in the end, his nutrition limits will be satisfied.

## User manual

### Main activity
On _MainActivity_ you see overview of your daily food intake, generated meal plan and manually added foods. You got four main options from this activity:

__1. Add food (floating button)__  
__2. Reset__  
__3. Settings__  
__4. (Re)Generate meal plan__

Other actions you can do on _MainActivity_:
* __Checking generated food__ – changes it's state and adds or subtracts it's nutrition values from daily intake
* __Swiping left manually added food__ – removes it from nutrition manager
* __Clicking on any (manual/generated) food__ – opens _OverviewActivity_ with details of given food

### Screenshots
TODO Main activity screenshots

### 1. Add food
Clicking food adding button gets you to a _FoodAddingActivity_, from where you can search and filter foods you want to manually add. ***To actually search for foods you have to hit search button on keyboard.*** Application doesn't give suggestions to keywords. This is ___intentional___ feature, trying to minimize API calls to API servers, because of daily [API request limit](#api-request-limit).
After you choose what you want to eat or have eaten, _OverviewActivity_ of given food will be shown so you can set quantity, serving size and meal where to add food. Hitting __ADD button__ adds food into your log.

#### Manually adding food for the first time a day
When you manually add food to your log for the first time in a day, you will be presented with 3 options to choose from. That happens because you are deviating from generated meal plan and application needs to know, how to act accordingly. These options are:
* _Cheatday_ – tells application to not bother with any adaptive re-generation settings for meal plan. Basically, choosing this option makes app ignore any manually added food.
* _Nextday_ – application will transfer any caloric excess from this day to next day at the end of the day. On next day, application will generate meal plan considering this caloric excess (will generate plan from your recommended daily caloric intake minus caloric excess from yesterday), so in total it will be the same caloric intake for both days.
* _Thisday_ – application will generate till the end of the day such meal plans, that considering manually added food, it's still able to keep to daily nutrition limits as much as possible.

Choosing one option makes it permanent till the end of the day or reset.

### 2. Reset
From main screen you got option (in action bar) to reset your daily food income and adaptive feature (if set). Simply press the button and you are back to 0s.

### 3. Settings
Clicking Settings action button presents you with 2 options:

__1. Nutrition settings__  
__2. User parameters__

#### Nutrition settings
In nutrition settings you can set your daily nutrition limits. You can choose from manual or automatic setting.
From manual you can set limits manually, but most of time, automatic is sufficent. If you choose automatic, then you are asked about your current lifestyle
(sedentary, mild activity, medium activity, high activity) and what is your weight goal (gain, lose, maintain). After entering these, app calculates your daily nutrition limit.
Prerequisite to this automatic calculation is entering your parameters in _UserParametersAcitivty_. If you haven't entered them, app won't let you do automatic calculation
and will ask you to enter them.

#### User parameters
For application correct functioning when using automatic nutrition settings, you are required to enter your physical parameters here.

### 4. (Re)Generate meal plan
After _User parameters_ and _Nutrition settings_ are set, clicking on __RE-GENERATE button__ generates a meal plan satisfying calculated nutrition daily limits as much as possible. If generated meal plan is unsatisfactory, clicking button again gives some other meal plan. Generated foods which are already checked won't be re-generated.

## Technical details

### Food types
Three food types are supported:  
__1. Product__  
__2. Recipe__  
__3. Restaurant food__

Recipes are recipes, restaurant foods are foods available from restaurants in user vicinity and products are everything else, which is not recipe or restaurant food.

Application supports __Restaurant food__ in USA regions. It means, if there are any restaurants nearby user, their food can be found in manual food adding by setting food type filter to _Restaurant_.

For now, application doesn't implement GPS support and setting of radius in which to consider restaurants. To tweak with this functionality, you have to change _NutritionixDMS_ class constant fields _coordinates_ and _radiusMeters_. 

### API request limit
By using [Nutritionix](https://www.nutritionix.com/business/api) and [Spoonacular](https://spoonacular.com/food-api) free plans, application is limited by their daily API requests limit. Application got default provided authorization keys for both services, but ***it's HIGHLY RECOMMENDED*** you provide your own keys so you can manage your API requests consumption.

#### Instructions to provide custom authorization keys for Nutritionix and Spoonacular


### Minimum Android Version
* Android 6.0 Marshmallow (API 23)

### Tested on devices
* Nexus 5X (API 29, x86 emulator)
* Huawei P8 Lite (Android 6.0, API 23, physical device)

## Known issues
TODO

## TODO
* Implement Database
* Implement Intake overview
* Use [View Binding](https://developer.android.com/topic/libraries/view-binding) :heavy_check_mark:
* Make activity hierarchy in AndroidManifest.xml to be able to use "back" action button
* In food adding dialog popup change number picker to number entering (number picker was used just for personal learning purposes)
* Use external solution with API for foods (right now I'm using just local database with cca. 1K of entries)

## Dataset
Powered by [Nutritionix](https://www.nutritionix.com/business/api) & [Spoonacular](https://spoonacular.com/food-api) API

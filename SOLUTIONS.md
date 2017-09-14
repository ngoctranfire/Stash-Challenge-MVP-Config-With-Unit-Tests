## Solutions
1. Converted basics to use RXJava. Didn't want to go too crazy so I only hooked in RXAdapter calls and GSON Converter Adapters.
2. Converted to Use MVP -> Persisted the activity through configuration changes and make sure that the MainPresenter.java survives configuration
3. If you are curious how ConfigPersistent works, take a look at BaseActivity and ConfigPersistentComponentManager

* Scope is as follows
```
<============================================> App Scope
    <==================================> ConfigPersistentScope
       <===============> <==============>
         Activity Scope    Activity Scope
```
4. Added unit tests. This is targetting the presenter, a.k.a, the pure business logic and making sure it calls the views correctly.
5. Also fixed a bunch of random issues that may not have been issues, but were issues to me...
    * Made Adapter -> Persist that at least for fragment so that I can scroll to the same scroll position when rotating.
6. What's missing? Future things needed -> UI Testing. Considering that the UI is actually quite dumb.... You just run a UI test. 
Inject a fake dagger graph that provides a fake Presenter... Feed the presenter data and test to verify UI state matches.

package me.y9san9.recycling.annotation

@RequiresOptIn(
    message = "This method uses unsafe KType. Consider to use alternative inline methods with reified generics",
    level = RequiresOptIn.Level.ERROR
)
annotation class UnsafeKType

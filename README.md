# macrotest
Create SQL like this:
```sql 
create type "medal" as enum ('Gold', 'Silver', 'Bronze');
```

from case object in scala file:
```scala
import enumeratum.Enum
import enumeratum.EnumEntry

sealed trait Medal extends EnumEntry
case object Medal extends Enum[Medal] {
  case object Gold extends Medal
  case object Silver extends Medal
  case object Bronze extends Medal
  lazy val values: IndexedSeq[Medal] = findValues
}
```

# Usage
```
sbt caseObjectToSqlEnum
```

# Settings
input: (in build.sbt)
```scala:build.sbt
lazy val input = "./src/main/scala/input/Input.scala"
```

output: (in build.sbt)
```scala:build.sbt
lazy val output = "./src/main/scala/output/output.sql"
```

# Note
this macro can run with
- sbt 1.6.1
- scala 2.13.7
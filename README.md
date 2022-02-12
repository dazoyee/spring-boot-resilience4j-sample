spring-boot-resilience4j-sample
---

# CircuitBreaker

> CircuitBreakerは接続先のシステムがダウン状態などで接続エラーが多発する際に、一定期間接続をさせずにそのままエラーを返すような仕組みを提供します。 接続先システムの状態をステータス管理しており、通常時はCLOSE, 接続エラーが一定割合を超えて発生するとOPEN, OPENから一定期間経つと接続先システムにつながるかを確かめるためのステータスHALF_OPEN状態になります。HALF_OPEN状態の時に一定割合を超えたエラーが継続する場合はOPENに、改善された場合はCLOSE状態に戻ります。

- `http://localhost:8080/success`
- `http://localhost:8080/failure`
- `http://localhost:8080/actuator/health`

# Bulkhead

> バルクヘッドは同時に実行可能を制限する機能です。

- `http://localhost:8080/delay`

# Retry

> 実行に失敗した際に再度実行する設定を行います。

- `http://localhost:8080/retry`

# RateLimiter

> 一定期間に実行可能な回数が指定されます。 その回数を超えた場合には、指定した時間待ちが発生し、待ちの後に受け入れ可能数に空きがあれば処理を行う、ない場合はエラーを返すものになります。

- `http://localhost:8080/rate-limit`
- `http://localhost:8080/actuator/health`

# TimeLimiter

> メソッドに対してタイムアウトを設定することができます。

- `http://localhost:8080/time-in`
- `http://localhost:8080/timeout`
